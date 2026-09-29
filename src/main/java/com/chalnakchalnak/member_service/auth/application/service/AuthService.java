package com.chalnakchalnak.member_service.auth.application.service;

import com.chalnakchalnak.member_service.auth.application.mapper.feign.MemberMapper;
import com.chalnakchalnak.member_service.auth.application.port.dto.SignOutDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.*;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.AuthResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.GetMemberIdResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.SignInResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.out.*;
import com.chalnakchalnak.member_service.auth.application.port.out.feign.member.MemberServicePort;
import com.chalnakchalnak.member_service.auth.domain.model.enums.IdentityVerificationPurpose;
import com.chalnakchalnak.member_service.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.member_service.auth.application.port.in.AuthUseCase;
import com.chalnakchalnak.member_service.auth.common.exception.BaseException;
import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import com.chalnakchalnak.member_service.auth.domain.model.AuthDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthRepositoryPort authRepositoryPort;
    private final AuthSecurityPort authSecurityPort;
    private final GenerateUuidPort generateUuidPort;
    private final VerificationCodeStorePort verificationCodeStorePort;
    private final TokenStorePort tokenStorePort;
    private final MemberServicePort memberServicePort;
    private final AuthMapper authMapper;
    private final MemberMapper memberMapper;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {

        if (!verificationCodeStorePort.grantedAccess(
                signUpRequestDto.getPhoneNumber(), IdentityVerificationPurpose.SIGN_UP.toString())
        ) {
            throw new BaseException(BaseResponseStatus.SIGN_UP_NOT_VERIFIED);
        }

        if(authRepositoryPort.existsByMemberId(signUpRequestDto.getMemberId())) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_MEMBER_ID);
        } else if (memberServicePort.existsByNickname(signUpRequestDto.getNickname())) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_NICKNAME);
        } else if (authRepositoryPort.existsByPhoneNumber(signUpRequestDto.getPhoneNumber())) {
            throw new BaseException(BaseResponseStatus.DUPLICATED_PHONE_NUMBER);
        }

        AuthDomain authDomain = authMapper.toAuthDomain(
                signUpRequestDto,
                generateUuidPort.generateUuid(),
                authSecurityPort.encryptPassword(signUpRequestDto.getPassword())
        );

        authRepositoryPort.save(authMapper.toSignUpDto(authDomain));

        // member-service로 member 생성 요청 (feign client)
        memberServicePort.createMember(memberMapper.toCreateMemberRequestDto(authDomain));
    }

    @Override
    public Boolean existsMemberId(ExistsMemberIdRequestDto existsMemberIdRequestDto) {
        return authRepositoryPort.existsByMemberId(existsMemberIdRequestDto.getMemberId());
    }

    @Override
    public Boolean existsNickname(ExistsNicknameRequestDto existsNicknameRequestDto) {
        return memberServicePort.existsByNickname(existsNicknameRequestDto.getNickname());
    }

    @Override
    public Boolean existsPhoneNumber(ExistsPhoneNumberRequestDto existsPhoneNumberRequestDto) {
        return authRepositoryPort.existsByPhoneNumber(existsPhoneNumberRequestDto.getPhoneNumber());
    }

    @Override
    @Transactional
    public SignInResponseDto signIn(SignInRequestDto authSignInRequestDto) {
        final AuthResponseDto authResponseDto =
                authRepositoryPort.findByMemberId(authSignInRequestDto.getMemberId())
                        .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND)
                        );

        final AuthDomain authDomain = authMapper.toAuthDomain(authResponseDto);

        final SignInResponseDto signInResponseDto = authSecurityPort.signIn(
                authMapper.toSignInDto(authDomain), authSignInRequestDto.getPassword()
        );

        tokenStorePort.saveRefreshToken(
                authMapper.toStoreRefreshTokenDto(authDomain.getMemberUuid(), signInResponseDto.getRefreshToken())
        );

        return signInResponseDto;
    }

    @Override
    @Transactional
    public SignInResponseDto reissueAllToken(ReissueAllTokenRequestDto reissueAllTokenRequestDto) {
        final String memberUuid = authSecurityPort.getMemberUuidByToken(reissueAllTokenRequestDto.getRefreshToken());

        if (!tokenStorePort.getRefreshToken(memberUuid)
                .equals(reissueAllTokenRequestDto.getRefreshToken())
        ) {
            System.out.println("refresh" + tokenStorePort.getRefreshToken(memberUuid));
            throw  new BaseException(BaseResponseStatus.INVALID_REFRESH_TOKEN);
        }

        final SignInResponseDto tokens = authSecurityPort.generateAllToken(memberUuid);

        tokenStorePort.saveRefreshToken(authMapper.toStoreRefreshTokenDto(memberUuid, tokens.getRefreshToken()));

        return tokens;
    }

    @Override
    @Transactional
    public void signOut(SignOutDto signOutDto) {
        try {
            final String memberUuid = authSecurityPort.getMemberUuidByToken(signOutDto.getRefreshToken());

            tokenStorePort.deleteRefreshToken(memberUuid);
        } catch (BaseException e) { }
    }

    @Override
    public GetMemberIdResponseDto getMemberId(GetMemberIdRequestDto getMemberIdRequestDto) {
        if (!verificationCodeStorePort.grantedAccess(
                getMemberIdRequestDto.getPhoneNumber(), IdentityVerificationPurpose.FIND_ID.toString())
        ) {
            throw new BaseException(BaseResponseStatus.FIND_MEMBER_ID_NOT_VERIFIED);
        }

        return authRepositoryPort.findMemberIdByPhoneNumber(
                authMapper.toGetMemberIdDto(getMemberIdRequestDto)
        ).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        if (!verificationCodeStorePort.grantedAccess(
                resetPasswordRequestDto.getPhoneNumber(), IdentityVerificationPurpose.PASSWORD_RESET.toString())
        ) {
            throw new BaseException(BaseResponseStatus.RESET_PASSWORD_NOT_VERIFIED);
        }

        authRepositoryPort.resetPassword(
                resetPasswordRequestDto.getPhoneNumber(),
                authSecurityPort.encryptPassword(resetPasswordRequestDto.getNewPassword())
        );
    }

}
