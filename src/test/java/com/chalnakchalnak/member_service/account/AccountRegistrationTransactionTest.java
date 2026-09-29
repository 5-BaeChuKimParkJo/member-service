package com.chalnakchalnak.member_service.account;

import com.chalnakchalnak.member_service.auth.adapter.out.persistence.mysql.repository.AuthJpaRepository;
import com.chalnakchalnak.member_service.grade.Grade;
import com.chalnakchalnak.member_service.grade.GradeRepository;
import com.chalnakchalnak.member_service.infrastructure.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
@SpringBootTest
class AccountRegistrationTransactionTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Autowired
    private AccountRegistrationService registrationService;
    @Autowired
    private AuthJpaRepository authRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GradeRepository gradeRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        authRepository.deleteAll();
        gradeRepository.deleteAll();
        gradeRepository.save(Grade.builder()
                .gradeUuid("grade-default")
                .gradeName("configured-default")
                .minPoint(0)
                .maxPoint(199)
                .orderNumber(5)
                .build());
    }

    @Test
    void rollsBackAuthWhenProfileInsertViolatesConstraint() {
        RegisterAccountCommand invalidProfile = new RegisterAccountCommand(
                "member-id",
                "plain-password",
                null,
                "01012345678"
        );

        assertThatThrownBy(() -> registrationService.register(invalidProfile))
                .isInstanceOf(DataIntegrityViolationException.class);

        assertThat(authRepository.count()).isZero();
        assertThat(memberRepository.count()).isZero();
    }
}
