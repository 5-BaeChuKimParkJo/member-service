# 프로필 이미지 조회 오류 처리

`ProfileImageExceptionHandler`는 이미지 조회 컨트롤러에만 적용한다. 공통
RuntimeException 처리기가 이미지의 ResponseStatusException까지 500으로 바꾸던
문제를 수정한다. 회원/인증/업로드 컨트롤러에는 적용하지 않는다.
[Spring의 advice 범위·순서 계약](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/web/bind/annotation/ControllerAdvice.html)
에 따라 컨트롤러 타입과 명시적 우선순위를 사용한다.

| 이미지 조회 상황 | HTTP / JSON code |
| --- | --- |
| key 누락·잘못된 형식, 파일의 미지원 Content-Type | 400 / 400 |
| 저장소가 없는 객체로 응답 | 404 / 404 |
| 기존 10 MiB 제한 초과 | 413 / 413 |
| 저장소 접근·연결·서버·스트림 IO 오류, 예상하지 못한 오류 | 500 / 500 |

어댑터 내부 upstream/IO 오류의 502 예외는 그대로이며 공개 응답은 합의한 500을
유지한다. 모든 ResponseStatusException을 그대로 통과시키는 전역 수정은 아니다.
기존 `code`, `message` 필드에 고정 문구를 사용하고 원문 예외·reason·키·호스트·
자격 증명을 반환하지 않는다. 새 500 경고 로그에는 예외 클래스만 남긴다.
오류 응답은 `Cache-Control: no-store`다. 정상 바이트·Content-Type·1년 immutable
캐시, 키 검증·읽기 제한, DB·MinIO·서명·업로드 설정은 수정하지 않는다.

## 로컬 검증 · 2026-10-29

- 실제 Spring/MockMvc → 컨트롤러·서비스·MinIO 어댑터를 실행하고 외부 AmazonS3만
  테스트 대역으로 바꿨다. 수정 전 새 18건 중 11건의 잘못된 500을 재현했다.
  연결 오류의 no-store 누락도 별도 실패를 관찰했다.
- 세 경로 별칭, 잘못된 키의 저장소 미접근, key 누락, 없는 객체, 미지원/빈
  Content-Type, 정확히 10 MiB 성공/초과 413, 정상 바이트/캐시 헤더, S3 접근·서버·
  SDK 연결·스트림 IO 오류와 고정 공개 메시지를 검증했다.
- 전체 로컬 Java 97건, ArchUnit·Spotless·bootJar, 커밋 규칙 도구 11건 통과.
  기존 회원/인증 오류 회귀 테스트도 포함한다.
- 프론트 기존 기본 프로필 테스트 6건 통과: 미설정 URL은 penguins.svg를 사용하고
  사용자 지정 사진 경로와 공개 에셋을 보존한다. 프론트 코드는 수정하지 않았다.
  임의의 깨진 URL을 복구하는 기능을 추가한 것은 아니다.
- 로컬 Docker는 실행하지 않았다. 기존 PostgreSQL Testcontainers 테스트 1건은
  로컬에서만 임시 제외하고 전체 CI는 원래 명령을 실행한다. MVC 인증 필터는
  격리를 위해 끄며 실제 MinIO outage·부하 시험은 아니다.
- 기존 @SpyBean 및 테스트의 @MockBean deprecation, JVM class-sharing 경고가 있다.

JDK 17에서 새 회귀 테스트만 실행하려면 다음을 사용한다.

```powershell
.\gradlew.bat test --tests "*ProfileImageHttpTest"
```
