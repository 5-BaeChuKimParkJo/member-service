package com.chalnakchalnak.member_service.account;

import com.chalnakchalnak.member_service.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.out.AuthRepositoryPort;
import com.chalnakchalnak.member_service.auth.application.port.out.AuthSecurityPort;
import com.chalnakchalnak.member_service.auth.application.port.out.GenerateUuidPort;
import com.chalnakchalnak.member_service.auth.common.exception.BaseException;
import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import com.chalnakchalnak.member_service.auth.domain.model.AuthDomain;
import com.chalnakchalnak.member_service.grade.Grade;
import com.chalnakchalnak.member_service.grade.GradePolicy;
import com.chalnakchalnak.member_service.grade.GradePolicyConfigurationException;
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
            throw new BaseException(BaseResponseStatus.DUPLICATED_MEMBER_ID);
        }
        if (memberProfiles.existsByNickname(command.nickname())) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_NICKNAME);
        }
        if (authRepository.existsByPhoneNumber(command.phoneNumber())) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_PHONE_NUMBER);
        }

        String memberUuid = uuidGenerator.generateUuid();
        AuthDomain auth = authMapper.toAuthDomain(
                SignUpRequestDto.builder()
                        .memberId(command.memberId())
                        .password(command.password())
                        .nickname(command.nickname())
                        .phoneNumber(command.phoneNumber())
                        .build(),
                memberUuid,
                authSecurity.encryptPassword(command.password())
        );
        Grade defaultGrade;
        try {
            defaultGrade = gradePolicy.gradeFor(INITIAL_POINTS);
            if (defaultGrade.getOrderNumber() != DEFAULT_GRADE_ORDER) {
                throw new GradePolicyConfigurationException(
                        "Initial points must resolve to grade order " + DEFAULT_GRADE_ORDER
                );
            }
        } catch (GradePolicyConfigurationException exception) {
            throw new BaseException(BaseResponseStatus.DEFAULT_GRADE_NOT_CONFIGURED);
        }

        authRepository.save(authMapper.toSignUpDto(auth));
        memberProfiles.createProfile(
                memberUuid,
                command.nickname(),
                defaultGrade.getGradeUuid(),
                INITIAL_POINTS
        );
    }
}
