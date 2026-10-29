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

## 독립 리뷰와 적용 범위

초기 구현 `ef3631a..0ccc995`의 독립 읽기 전용 리뷰에서 Critical/Important/Minor
지적은 없었다. 리뷰어가 검사나 배포를 실행한 것은 아니며 실행 근거는 작성자가
별도로 확인한다.

기존 키 정규식의 허용 범위, 다른 API의 필수 헤더·메서드·미디어 협상·프레임워크
오류 전체 처리, 서명·업로드 정책, 캐시 TTL·무효화와 임의의 깨진 사진 URL 복구는
이번 변경에 포함하지 않는다. 인증 필터/정책 및 기존 다른 로그의 정비도 별도다.
따라서 이 작업이 모든 잘못된 입력·인증 장애·캐시 장애를 복구하거나 전체 로그의
민감정보 노출을 검증한 것은 아니다. 실제 MinIO outage/부하와 PostgreSQL 전체 CI,
이미지 발행·배포는 정적 리뷰와 구분한 검증 근거가 필요하다.

## 실제 CI · 2026-10-29

런타임 커밋은 `0ccc995de7ccefb92b8daf62fd466975f040bc7d`다.
[CI 40887317311](https://github.com/5-BaeChuKimParkJo/member-service/actions/runs/40887317311)
에서 전체 `./gradlew clean check bootJar`와 GHCR 발행이 성공했다. 실제 test task와
`BUILD SUCCESSFUL in 1m 2s`를 확인했다. 로컬의 Docker 테스트 제외 설정을 CI에
전달하지 않았고 기존 PostgreSQL 트랜잭션 테스트도 원래대로 전체 검사 대상이다.

## 실제 배포·읽기 전용 확인

자동 GitOps 커밋 `7ea636d1babb4cc81bc2e0a1072d02941bda03ab`는 account-service
태그만 갱신했다. 실제 실행 이미지가 `0ccc995`인지, 새 파드 Ready=true/재시작 0과
rollout 완료, Argo Synced/Healthy를 확인했다. 초기 Spring 기동은 첫 50초 대기를
넘겼고 readiness 연결 거부/시간 초과가 있었다. 이미지 다운로드와 준비 상태를
확인한 뒤 실제 완료를 다시 확인했다.

- 같은 잘못된 키·없는 객체 조회는 배포 전 각각 500이었고, 배포 후 400·404였다.
  두 외부 경로에서 key 누락도 포함한 6건 모두 해당 코드와 no-store를 확인했다.
- penguins.svg 공개 에셋 200과 실제 익명 경매 화면의 기본 판매자 이미지를
  데스크톱/모바일에서 확인했다. 이미지 complete/naturalWidth로 로드 완료를
  검사했고 입찰·결제·업로드 POST는 0건이었다.
- 기존 회원 조회는 없는 회원 404/code2002, 기존 판매자 200을 유지했다.
  인증/인증번호 네 경로의 잘못된 메서드는 405/code4902를 유지했다.
- 최대 8개 기존 경매 조회 범위에서 확인 가능한 기존 업로드 사진 샘플을 찾지
  못해 실제 업로드 객체의 200은 확인하지 않았다. 정상 바이트·캐시 및 413/500
  실패는 격리 테스트 근거다. 검증용 운영 업로드·삭제·장애 주입은 하지 않았다.

GitOps 로컬을 자동 커밋에 동기화했으며 프론트 소스와 런타임 이미지는 그대로다.
