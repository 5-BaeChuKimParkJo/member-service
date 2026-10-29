# 프로필 조회 캐시 장애 대응

`MemberService.getMember`에 붙은 `member:v2` 캐시의 읽기 오류는 miss로 취급해
기존 JPA 조회를 실행한다. DB 결과를 캐시에 저장하다 실패해도 정상 프로필을 반환한다.
복구 후 다음 조회부터 캐시 저장/hit를 다시 사용한다.

`MemberCacheConfig`가 Spring의 `CachingConfigurer`를 통해 오류 처리기를 연결한다.
기본 `@Cacheable`은 읽기 실패 후 별도 저장도 시도하므로 두 동작을 처리한다.
[Spring 공식 계약](https://docs.spring.io/spring-framework/docs/6.2.x/javadoc-api/org/springframework/cache/interceptor/CacheErrorHandler.html)
에 따른 방식이며 비즈니스 메서드나 DB 포트를 변경하지 않는다.

범위는 `member:v2` get/put이다. 다른 캐시와 evict/clear 오류는 원래 예외를 던진다.
직접 RedisTemplate를 사용하는 인증 토큰/인증번호나 프로필 변경의 무효화 정책은
별도 경계다. DB 오류·없는 회원 예외를 가짜 프로필 성공으로 바꾸지 않는다.
새 경고 로그에는 동작과 예외 클래스만 기록하며 키·값·회원 정보·원문 오류를 제외한다.

## 검증

- 실제 Spring 캐시 프록시/MockMvc/JPA/H2에 Cache provider 오류를 주입했다.
  cache read/write/both 실패에서 200, 정상 cache hit, 복구 후 재저장/hit,
  DB 오류와 없는 회원의 기존 응답 보존을 확인했다.
- 허용 목록 밖 get/put, invalidation 예외 전파와 로그의 민감값 미포함도 검사했다.
- 수정 전 7건 중 6건 실패를 관찰하고 수정 후 전체 로컬 53건과 ArchUnit,
  Spotless, bootJar를 통과했다. 커밋 검사 도구 테스트도 11건 통과했다.
- 로컬에서는 Docker를 띄우지 않았다. 기존 `AccountRegistrationTransactionTest`
  1건은 명시적으로 제외하고 전체 Linux CI에서 별도로 실행한다.

새 회귀 테스트만 실행할 때는 JDK 17에서 다음을 사용한다. Redis 서버나 Docker는 필요 없다.

```powershell
.\gradlew.bat test --tests "*MemberProfileCache*Test"
```

독립 검토에서 Critical/Important/Minor 지적은 없었다. 검토자는 테스트를 직접 실행하지
않았으며, 실행 근거와 실제 배포는 작성자가 별도로 확인한다. HTTP 캐시 테스트의 인증
필터는 격리를 위해 꺼져 있다. 인증 저장소 장애에서 로그인 성공을 보장하는 기능은 아니다.

## 한계와 별도 범위

Redis 명령이 오류를 반환할 때의 fallback이다. 기존 명령 타임아웃까지의 대기시간과
outage 때 늘어나는 DB 조회 부하는 남는다. 타임아웃·TTL·재시도·스키마는 수정하지 않는다.
테스트는 실제 Redis를 끄는 네트워크 장애 시험이 아니다. 운영 Redis에도 장애를 주입하지 않는다.

캐시 fallback 배포 당시 없는 회원의 예외는 회원용 BaseException이었으나 전역 HTTP
매핑은 500이었다. 이후 [회원 API 오류 응답 분리](member-error-mapping.md)에서 404로
수정했다. 현재 회귀 테스트는 정상 캐시와 장애 상태 모두 404/code 2002이며 가짜
성공이 아님을 확인한다. 이 변경은 캐시 fallback 정책과 분리된 HTTP 예외 매핑이다.

## 실제 CI·운영 확인 · 2026-10-29

구현 커밋은 `9d2f7b76e9355d6154c35364b1b58d53c785901c`다.
[CI 40832095706](https://github.com/5-BaeChuKimParkJo/member-service/actions/runs/40832095706)
에서 `./gradlew clean check bootJar`와 GHCR 발행이 모두 성공했다. 로컬의 임시 제외
설정은 CI에 전달하지 않았으며 기존 Docker/PostgreSQL 트랜잭션 테스트도 전체 실행 대상이다.
실제 로그의 test task 실행과 `BUILD SUCCESSFUL in 1m 18s`를 확인했다.

자동 GitOps 리비전 `1bfb00d1d3d375cb53c9891fd19101082f0091e8`에서 실제 account-service
이미지 `9d2f7b7`과 rollout 완료를 확인했다. 새 파드는 준비 1/1, 재시작 0회였고
Argo는 Synced/Healthy였다. 초기 기동은 첫 55초 대기 제한을 넘겼고, 이벤트와 새 파드
준비 상태를 재확인한 뒤 완료를 판정했다.

실제 운영 Chromium 데스크톱/모바일에서 익명 판매자 조회 200과 경매 판매자 UUID
일치, 경매 제목·판매자 표시, 가로 넘침 없음을 확인했다. 입찰/결제 POST는 0건이다.
운영 Redis를 중단하지 않았으므로 이 화면 확인은 실제 Redis 장애 주입 시험이 아니다.
