package com.chalnakchalnak.member_service.account;

import com.chalnakchalnak.member_service.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.member_service.auth.application.port.dto.SignUpDto;
import com.chalnakchalnak.member_service.auth.application.port.out.AuthRepositoryPort;
import com.chalnakchalnak.member_service.auth.application.port.out.AuthSecurityPort;
import com.chalnakchalnak.member_service.auth.application.port.out.GenerateUuidPort;
import com.chalnakchalnak.member_service.auth.common.exception.BaseException;
import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import com.chalnakchalnak.member_service.grade.Grade;
import com.chalnakchalnak.member_service.grade.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountRegistrationServiceTest {

    @Mock
    private AuthRepositoryPort authRepository;
    @Mock
    private AuthSecurityPort authSecurity;
    @Mock
    private GenerateUuidPort uuidGenerator;
    @Mock
    private MemberProfilePort memberProfiles;
    @Mock
    private GradeRepository grades;

    private AccountRegistrationService service;

    @BeforeEach
    void setUp() {
        service = new AccountRegistrationService(
                authRepository,
                authSecurity,
                uuidGenerator,
                memberProfiles,
                grades,
                new AuthMapper()
        );
    }

    @Test
    void registersAuthAndProfileWithConfiguredDefaultGrade() {
        Grade defaultGrade = Grade.builder()
                .gradeUuid("grade-default")
                .gradeName("configured-default")
                .minPoint(0)
                .maxPoint(199)
                .orderNumber(5)
                .build();
        when(uuidGenerator.generateUuid()).thenReturn("member-uuid");
        when(authSecurity.encryptPassword("plain-password")).thenReturn("hashed-password");
        when(grades.findByOrderNumber(5)).thenReturn(Optional.of(defaultGrade));

        service.register(command());

        ArgumentCaptor<SignUpDto> auth = ArgumentCaptor.forClass(SignUpDto.class);
        verify(authRepository).save(auth.capture());
        assertThat(auth.getValue().getMemberUuid()).isEqualTo("member-uuid");
        assertThat(auth.getValue().getPassword()).isEqualTo("hashed-password");
        verify(memberProfiles).createProfile("member-uuid", "nickname", "grade-default", 100.0);
    }

    @Test
    void rejectsDuplicateMemberId() {
        when(authRepository.existsByMemberId("member-id")).thenReturn(true);

        assertStatus(BaseResponseStatus.DUPLICATED_MEMBER_ID);
    }

    @Test
    void rejectsDuplicateNickname() {
        when(memberProfiles.existsByNickname("nickname")).thenReturn(true);

        assertStatus(BaseResponseStatus.DUPLICATED_NICKNAME);
    }

    @Test
    void rejectsDuplicatePhoneNumber() {
        when(authRepository.existsByPhoneNumber("01012345678")).thenReturn(true);

        assertStatus(BaseResponseStatus.DUPLICATED_PHONE_NUMBER);
    }

    @Test
    void failsClearlyWhenDefaultGradeIsMissing() {
        when(uuidGenerator.generateUuid()).thenReturn("member-uuid");
        when(authSecurity.encryptPassword("plain-password")).thenReturn("hashed-password");
        when(grades.findByOrderNumber(5)).thenReturn(Optional.empty());

        assertStatus(BaseResponseStatus.DEFAULT_GRADE_NOT_CONFIGURED);
        verify(authRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(memberProfiles, never()).createProfile(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyDouble()
        );
    }

    private void assertStatus(BaseResponseStatus status) {
        assertThatThrownBy(() -> service.register(command()))
                .isInstanceOfSatisfying(BaseException.class,
                        exception -> assertThat(exception.getStatus()).isEqualTo(status));
    }

    private RegisterAccountCommand command() {
        return new RegisterAccountCommand(
                "member-id",
                "plain-password",
                "nickname",
                "01012345678"
        );
    }
}
