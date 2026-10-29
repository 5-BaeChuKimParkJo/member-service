# 회원 API 오류 응답 분리

회원이 없는데도 500으로 응답하던 문제를 수정했다. 인증용·공통 예외 처리기가
모두 전역으로 등록되어, 먼저 선택된 처리기의 RuntimeException fallback이 다른
영역의 도메인 예외를 가로채고 있었다. 순서만 뒤집으면 인증 오류가 반대로 500이 된다.

인증 처리기는 `auth.adapter.in.web` 패키지로 제한하고 우선순위를 명시한다.
공통 처리기는 가장 낮은 우선순위의 전역 fallback으로 남긴다.
[Spring의 advice 선택·우선순위 계약](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/web/bind/annotation/ControllerAdvice.html)
을 사용하며 새로운 예외 모델이나 의존성을 추가하지 않는다.

| 상황 | HTTP | JSON code |
| --- | --- | --- |
| 없는 회원 프로필 조회 | 404 | 2002 |
| 닉네임 중복 / 현재 닉네임 재선택 | 409 | 2001 / 2004 |
| 회원 API의 예상하지 못한 DB·런타임 오류 | 500 | 500 |
| 기존 인증 실패 / 잘못된 자격 증명 | 401 | 4204 |
| 기존 인증 API의 예상하지 못한 오류 | 500 | 4903 |
| 만료된 인증번호 | 400 | 4200 |

오류 응답 필드는 기존 `code`, `message`다. `httpStatus`는 JSON에 노출하지 않는다.
회원 서버 오류의 공개 메시지는 고정 문구이며 DB 연결·SQL 등의 원문 예외 메시지를
반환하지 않는다. 원인 예외는 서버 로거에 남긴다. 일반 오류를 존재하지 않는 회원으로
바꾸거나 예외를 정상 프로필로 반환하지 않는다.

회원 도메인 404/409와 공통 오류 코드가 이제 해당 API에서 일관되게 선택된다.
특히 회원의 일반 서버 오류는 인증 코드 4903이 아닌 회원 공통 코드 500이다.
인증/인증번호의 기존 코드와 메시지는 유지한다. 경로, 성공 응답, DB 스키마,
인증 필터와 비즈니스 규칙은 수정하지 않는다.

## 로컬 검증 · 2026-10-29

- 수정 전 대상 23건 중 11건이 실패했다. 회원 404/409, 인증 401·유효성 검사,
  오류 코드 선택과 실제 Spring/H2의 없는 회원 500을 재현했다.
- 실제 컨트롤러·매퍼·advice를 사용해 등록 순서를 뒤집어도 결과가 같은지 검사했다.
  외부 호출 대신 애플리케이션 포트에서 도메인/DB 오류를 주입했다.
- 실제 Spring 컨텍스트·JPA/H2·캐시 프록시 테스트에서 회원 경로 3개 모두 캐시
  정상/장애 시 404와 code 2002, 캐시 장애 중 DB 오류 500을 확인했다.
- 전체 로컬 73건, ArchUnit, Spotless, bootJar와 커밋 규칙 도구 11건이 통과했다.
  기존 `@SpyBean` deprecation과 JVM class-sharing 경고는 남아 있다.
- 로컬 Docker는 띄우지 않았다. 기존 PostgreSQL Testcontainers 테스트 1건은
  로컬 실행에서만 임시 제외했고, CI의 원래 `clean check bootJar`에는 제외하지 않는다.

JDK 17에서 새 회귀 테스트만 실행하려면 다음 명령을 사용한다.

```powershell
.\gradlew.bat test --tests "*MemberErrorResponseTest" --tests "*MemberProfileCacheFallbackTest"
```

MVC 경계 테스트는 인증 필터를 실행하지 않는다. 토큰 발급·재발급 및 로그인 실패
정책 자체를 바꾼 작업이 아니다. 실제 장애를 운영 DB/Redis에 주입하지 않는다.
