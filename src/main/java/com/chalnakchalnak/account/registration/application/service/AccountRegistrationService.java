package com.chalnakchalnak.account.registration.application.service;

import com.chalnakchalnak.account.auth.application.command.SignUpCommand;
import com.chalnakchalnak.account.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.account.auth.application.port.out.AuthRepositoryPort;
import com.chalnakchalnak.account.auth.application.port.out.AuthSecurityPort;
import com.chalnakchalnak.account.auth.application.port.out.GenerateUuidPort;
import com.chalnakchalnak.account.auth.common.exception.BaseException;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import com.chalnakchalnak.account.auth.domain.model.AuthAccount;
import com.chalnakchalnak.account.grade.application.service.GradePolicy;
import com.chalnakchalnak.account.grade.domain.GradeDefinition;
import com.chalnakchalnak.account.grade.domain.GradePolicyConfigurationException;
import com.chalnakchalnak.account.registration.application.command.RegisterAccountCommand;
import com.chalnakchalnak.account.registration.application.port.out.MemberProfilePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountRegistrationService {

  static final int DEFAULT_GRADE_ORDER = 5;
  static final double INITIAL_POINTS = 100.0;

  private final AuthRepositoryPort authRepository;
  private final AuthSecurityPort authSecurity;
  private final GenerateUuidPort uuidGenerator;
  private final MemberProfilePort memberProfiles;
  private final GradePolicy gradePolicy;
  private final AuthMapper authMapper;

  @Transactional
  public void register(RegisterAccountCommand command) {
    if (authRepository.existsByMemberId(command.memberId())) {
      throw new BaseException(AuthErrorCode.DUPLICATED_MEMBER_ID);
    }
    if (memberProfiles.existsByNickname(command.nickname())) {
      throw new BaseException(AuthErrorCode.DUPLICATED_NICKNAME);
    }
    if (authRepository.existsByPhoneNumber(command.phoneNumber())) {
      throw new BaseException(AuthErrorCode.DUPLICATED_PHONE_NUMBER);
    }

    String memberUuid = uuidGenerator.generateUuid();
    AuthAccount auth =
        authMapper.toAuthDomain(
            SignUpCommand.builder()
                .memberId(command.memberId())
                .password(command.password())
                .nickname(command.nickname())
                .phoneNumber(command.phoneNumber())
                .build(),
            memberUuid,
            authSecurity.encryptPassword(command.password()));
    GradeDefinition defaultGrade;
    try {
      defaultGrade = gradePolicy.gradeFor(INITIAL_POINTS);
      if (defaultGrade.getOrderNumber() != DEFAULT_GRADE_ORDER) {
        throw new GradePolicyConfigurationException(
            "Initial points must resolve to grade order " + DEFAULT_GRADE_ORDER);
      }
    } catch (GradePolicyConfigurationException exception) {
      throw new BaseException(AuthErrorCode.DEFAULT_GRADE_NOT_CONFIGURED);
    }

    authRepository.save(authMapper.toSignUpDto(auth));
    memberProfiles.createProfile(
        memberUuid, command.nickname(), defaultGrade.getGradeUuid(), INITIAL_POINTS);
  }
}
