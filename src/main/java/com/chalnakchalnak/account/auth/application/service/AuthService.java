package com.chalnakchalnak.account.auth.application.service;

import com.chalnakchalnak.account.auth.application.command.ReissueAllTokenCommand;
import com.chalnakchalnak.account.auth.application.command.ResetPasswordCommand;
import com.chalnakchalnak.account.auth.application.command.SignInCommand;
import com.chalnakchalnak.account.auth.application.command.SignOutCommand;
import com.chalnakchalnak.account.auth.application.command.SignUpCommand;
import com.chalnakchalnak.account.auth.application.mapper.AuthMapper;
import com.chalnakchalnak.account.auth.application.port.in.AuthUseCase;
import com.chalnakchalnak.account.auth.application.port.out.*;
import com.chalnakchalnak.account.auth.application.query.ExistsMemberIdQuery;
import com.chalnakchalnak.account.auth.application.query.ExistsNicknameQuery;
import com.chalnakchalnak.account.auth.application.query.ExistsPhoneNumberQuery;
import com.chalnakchalnak.account.auth.application.query.GetMemberIdQuery;
import com.chalnakchalnak.account.auth.application.result.AuthResult;
import com.chalnakchalnak.account.auth.application.result.MemberIdResult;
import com.chalnakchalnak.account.auth.application.result.SignInResult;
import com.chalnakchalnak.account.auth.common.exception.BaseException;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import com.chalnakchalnak.account.auth.domain.model.AuthAccount;
import com.chalnakchalnak.account.auth.domain.model.enums.IdentityVerificationPurpose;
import com.chalnakchalnak.account.registration.application.command.RegisterAccountCommand;
import com.chalnakchalnak.account.registration.application.port.out.MemberProfilePort;
import com.chalnakchalnak.account.registration.application.service.AccountRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

  private final AuthRepositoryPort authRepositoryPort;
  private final AuthSecurityPort authSecurityPort;
  private final VerificationCodeStorePort verificationCodeStorePort;
  private final TokenStorePort tokenStorePort;
  private final AccountRegistrationService accountRegistrationService;
  private final MemberProfilePort memberProfilePort;
  private final AuthMapper authMapper;

  @Override
  public void signUp(SignUpCommand signUpRequestDto) {

    if (!verificationCodeStorePort.grantedAccess(
        signUpRequestDto.getPhoneNumber(), IdentityVerificationPurpose.SIGN_UP.toString())) {
      throw new BaseException(AuthErrorCode.SIGN_UP_NOT_VERIFIED);
    }

    accountRegistrationService.register(
        new RegisterAccountCommand(
            signUpRequestDto.getMemberId(),
            signUpRequestDto.getPassword(),
            signUpRequestDto.getNickname(),
            signUpRequestDto.getPhoneNumber()));
  }

  @Override
  public Boolean existsMemberId(ExistsMemberIdQuery existsMemberIdRequestDto) {
    return authRepositoryPort.existsByMemberId(existsMemberIdRequestDto.getMemberId());
  }

  @Override
  public Boolean existsNickname(ExistsNicknameQuery existsNicknameRequestDto) {
    return memberProfilePort.existsByNickname(existsNicknameRequestDto.getNickname());
  }

  @Override
  public Boolean existsPhoneNumber(ExistsPhoneNumberQuery existsPhoneNumberRequestDto) {
    return authRepositoryPort.existsByPhoneNumber(existsPhoneNumberRequestDto.getPhoneNumber());
  }

  @Override
  @Transactional
  public SignInResult signIn(SignInCommand authSignInRequestDto) {
    final AuthResult authResponseDto =
        authRepositoryPort
            .findByMemberId(authSignInRequestDto.getMemberId())
            .orElseThrow(() -> new BaseException(AuthErrorCode.USER_NOT_FOUND));

    final AuthAccount authDomain = authMapper.toAuthDomain(authResponseDto);

    final SignInResult signInResponseDto =
        authSecurityPort.signIn(
            authMapper.toSignInDto(authDomain), authSignInRequestDto.getPassword());

    tokenStorePort.saveRefreshToken(
        authMapper.toStoreRefreshTokenDto(
            authDomain.getMemberUuid(), signInResponseDto.getRefreshToken()));

    return signInResponseDto;
  }

  @Override
  @Transactional
  public SignInResult reissueAllToken(ReissueAllTokenCommand reissueAllTokenRequestDto) {
    final String memberUuid =
        authSecurityPort.getMemberUuidByRefreshToken(reissueAllTokenRequestDto.getRefreshToken());

    if (!tokenStorePort
        .getRefreshToken(memberUuid)
        .equals(reissueAllTokenRequestDto.getRefreshToken())) {
      System.out.println("refresh" + tokenStorePort.getRefreshToken(memberUuid));
      throw new BaseException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    final SignInResult tokens = authSecurityPort.generateAllToken(memberUuid);

    tokenStorePort.saveRefreshToken(
        authMapper.toStoreRefreshTokenDto(memberUuid, tokens.getRefreshToken()));

    return tokens;
  }

  @Override
  @Transactional
  public void signOut(SignOutCommand signOutDto) {
    try {
      final String memberUuid =
          authSecurityPort.getMemberUuidByRefreshToken(signOutDto.getRefreshToken());

      tokenStorePort.deleteRefreshToken(memberUuid);
    } catch (BaseException e) {
    }
  }

  @Override
  public MemberIdResult getMemberId(GetMemberIdQuery getMemberIdRequestDto) {
    if (!verificationCodeStorePort.grantedAccess(
        getMemberIdRequestDto.getPhoneNumber(), IdentityVerificationPurpose.FIND_ID.toString())) {
      throw new BaseException(AuthErrorCode.FIND_MEMBER_ID_NOT_VERIFIED);
    }

    return authRepositoryPort
        .findMemberIdByPhoneNumber(authMapper.toGetMemberIdDto(getMemberIdRequestDto))
        .orElseThrow(() -> new BaseException(AuthErrorCode.USER_NOT_FOUND));
  }

  @Override
  public void resetPassword(ResetPasswordCommand resetPasswordRequestDto) {
    if (!verificationCodeStorePort.grantedAccess(
        resetPasswordRequestDto.getPhoneNumber(),
        IdentityVerificationPurpose.PASSWORD_RESET.toString())) {
      throw new BaseException(AuthErrorCode.RESET_PASSWORD_NOT_VERIFIED);
    }

    authRepositoryPort.resetPassword(
        resetPasswordRequestDto.getPhoneNumber(),
        authSecurityPort.encryptPassword(resetPasswordRequestDto.getNewPassword()));
  }
}
