package com.chalnakchalnak.account.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.chalnakchalnak.account.auth.application.command.StoreAuthCommand;
import com.chalnakchalnak.account.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.account.auth.application.port.out.AuthRepositoryPort;
import com.chalnakchalnak.account.auth.application.port.out.AuthSecurityPort;
import com.chalnakchalnak.account.auth.application.port.out.GenerateUuidPort;
import com.chalnakchalnak.account.auth.common.exception.BaseException;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import com.chalnakchalnak.account.grade.application.port.out.GradeRepositoryPort;
import com.chalnakchalnak.account.grade.application.service.GradePolicy;
import com.chalnakchalnak.account.grade.domain.GradeDefinition;
import com.chalnakchalnak.account.registration.application.command.RegisterAccountCommand;
import com.chalnakchalnak.account.registration.application.port.out.MemberProfilePort;
import com.chalnakchalnak.account.registration.application.service.AccountRegistrationService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountRegistrationServiceTest {

  @Mock private AuthRepositoryPort authRepository;
  @Mock private AuthSecurityPort authSecurity;
  @Mock private GenerateUuidPort uuidGenerator;
  @Mock private MemberProfilePort memberProfiles;
  @Mock private GradeRepositoryPort grades;

  private AccountRegistrationService service;

  @BeforeEach
  void setUp() {
    service =
        new AccountRegistrationService(
            authRepository,
            authSecurity,
            uuidGenerator,
            memberProfiles,
            new GradePolicy(grades),
            new AuthMapper());
  }

  @Test
  void registersAuthAndProfileWithConfiguredDefaultGrade() {
    GradeDefinition defaultGrade =
        GradeDefinition.builder()
            .gradeUuid("grade-default")
            .gradeName("configured-default")
            .minPoint(0)
            .maxPoint(199)
            .orderNumber(5)
            .build();
    when(uuidGenerator.generateUuid()).thenReturn("member-uuid");
    when(authSecurity.encryptPassword("plain-password")).thenReturn("hashed-password");
    when(grades.findAll()).thenReturn(List.of(defaultGrade));

    service.register(command());

    ArgumentCaptor<StoreAuthCommand> auth = ArgumentCaptor.forClass(StoreAuthCommand.class);
    verify(authRepository).save(auth.capture());
    assertThat(auth.getValue().getMemberUuid()).isEqualTo("member-uuid");
    assertThat(auth.getValue().getPassword()).isEqualTo("hashed-password");
    verify(memberProfiles).createProfile("member-uuid", "nickname", "grade-default", 100.0);
  }

  @Test
  void rejectsDuplicateMemberId() {
    when(authRepository.existsByMemberId("member-id")).thenReturn(true);

    assertStatus(AuthErrorCode.DUPLICATED_MEMBER_ID);
  }

  @Test
  void rejectsDuplicateNickname() {
    when(memberProfiles.existsByNickname("nickname")).thenReturn(true);

    assertStatus(AuthErrorCode.DUPLICATED_NICKNAME);
  }

  @Test
  void rejectsDuplicatePhoneNumber() {
    when(authRepository.existsByPhoneNumber("01012345678")).thenReturn(true);

    assertStatus(AuthErrorCode.DUPLICATED_PHONE_NUMBER);
  }

  @Test
  void failsClearlyWhenDefaultGradeIsMissing() {
    when(uuidGenerator.generateUuid()).thenReturn("member-uuid");
    when(authSecurity.encryptPassword("plain-password")).thenReturn("hashed-password");
    when(grades.findAll()).thenReturn(List.of());

    assertStatus(AuthErrorCode.DEFAULT_GRADE_NOT_CONFIGURED);
    verify(authRepository, never()).save(org.mockito.ArgumentMatchers.any());
    verify(memberProfiles, never())
        .createProfile(
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyDouble());
  }

  @Test
  void rejectsDefaultGradeThatDoesNotContainInitialPoints() {
    GradeDefinition invalidDefault =
        GradeDefinition.builder()
            .gradeUuid("grade-default")
            .gradeName("invalid-default")
            .minPoint(200)
            .maxPoint(299)
            .orderNumber(5)
            .build();
    when(uuidGenerator.generateUuid()).thenReturn("member-uuid");
    when(authSecurity.encryptPassword("plain-password")).thenReturn("hashed-password");
    when(grades.findAll()).thenReturn(List.of(invalidDefault));

    assertStatus(AuthErrorCode.DEFAULT_GRADE_NOT_CONFIGURED);
    verify(authRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  private void assertStatus(AuthErrorCode status) {
    assertThatThrownBy(() -> service.register(command()))
        .isInstanceOfSatisfying(
            BaseException.class, exception -> assertThat(exception.getStatus()).isEqualTo(status));
  }

  private RegisterAccountCommand command() {
    return new RegisterAccountCommand("member-id", "plain-password", "nickname", "01012345678");
  }
}
