# 찰낙찰낙 · Account Service

인증·회원·프로필을 통합한 Spring Boot 서비스다. 소스 저장소 이름은
`member-service`, Kubernetes·실행·이미지 이름은 `account-service`다.
기준일: 2026-10-25.

## 역할과 연결

- 회원가입·로그인·JWT와 회원 프로필 처리.
- PostgreSQL `cn_member`, Redis 인증 관련 상태, MinIO 프로필 이미지 사용.
- 프론트와 관리자 BFF가 필요한 회원 식별 정보 제공.
- 홈랩은 고정 인증번호 모드이며 실 SMS 발송과 구분한다.

기존 `/auth-service`, `/member-service` 및 `/account-service` 공개 경로는
Kong에서 이 서비스로 연결한다. 각 경로마다 별도 Pod를 띄운 구성이 아니다.
DB 엔진은 PostgreSQL이며 현재 홈랩에 MySQL을 추가 배포하지 않는다.

## 구조와 로컬 실행

업무 기능의 `domain / application / adapter` 의존 방향을 따른다.
런타임·DB·HTTP 이름은 호환 경계이므로 저장소 이름을 바꾸듯 일괄 변경하지 않는다.

JDK 17을 사용한다. `POSTGRES_URL`, `POSTGRES_USERNAME`, `POSTGRES_PASSWORD`,
Redis·JWT·스토리지 등의 환경은 `src/main/resources/application.yml`을 기준으로
로컬 의존 서비스에 맞춰 준비한다.

```powershell
.\gradlew.bat bootRun
.\gradlew.bat clean check bootJar
```

트랜잭션 통합 테스트에는 Docker가 필요하다. 운영 DB를 테스트 대상으로 지정하지 않는다.

## 배포와 문서

main CI가 `ghcr.io/5-baechukimparkjo/account-service:<commit-sha>`를 게시한다.
GitOps의 이미지 태그 변경과 Argo CD 동기화 후 실제 실행 버전이 바뀐다.

- [전체 아키텍처](https://github.com/5-BaeChuKimParkJo/FE-Service/blob/main/docs/portfolio/architecture.md)
- [체험 순서](https://github.com/5-BaeChuKimParkJo/FE-Service/blob/main/docs/portfolio/demo-guide.md)
- [운영 SQL 설명](ops/sql/README.md)
- [비공개 GitOps](https://github.com/chuman0216/cncn_private)
- [코드·커밋 규칙](CONTRIBUTING.md)
