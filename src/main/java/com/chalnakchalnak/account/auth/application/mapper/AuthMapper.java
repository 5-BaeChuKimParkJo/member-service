package com.chalnakchalnak.account.auth.application.mapper;

import com.chalnakchalnak.account.auth.application.command.SignUpCommand;
import com.chalnakchalnak.account.auth.application.command.StoreAuthCommand;
import com.chalnakchalnak.account.auth.application.command.StoreRefreshTokenCommand;
import com.chalnakchalnak.account.auth.application.query.AuthenticateAccountQuery;
import com.chalnakchalnak.account.auth.application.query.GetMemberIdQuery;
import com.chalnakchalnak.account.auth.application.query.MemberIdLookupQuery;
import com.chalnakchalnak.account.auth.application.result.AuthResult;
import com.chalnakchalnak.account.auth.application.result.SignInResult;
import com.chalnakchalnak.account.auth.domain.model.AuthAccount;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

  public AuthAccount toAuthDomain(
      SignUpCommand signUpRequestDto, String uuid, String hashedPassword) {
    return AuthAccount.builder()
        .memberUuid(uuid)
        .memberId(signUpRequestDto.getMemberId())
        .password(hashedPassword)
        .nickname(signUpRequestDto.getNickname())
        .phoneNumber(signUpRequestDto.getPhoneNumber())
        .build();
  }

  public AuthAccount toAuthDomain(AuthResult authResponseDto) {
    return AuthAccount.builder()
        .memberUuid(authResponseDto.getMemberUuid())
        .memberId(authResponseDto.getMemberId())
        .password(authResponseDto.getPassword())
        .nickname("")
        .phoneNumber(authResponseDto.getPhoneNumber())
        .build();
  }

  public StoreAuthCommand toSignUpDto(AuthAccount authDomain) {
    return StoreAuthCommand.builder()
        .memberUuid(authDomain.getMemberUuid())
        .memberId(authDomain.getMemberId())
        .password(authDomain.getPassword())
        .phoneNumber(authDomain.getPhoneNumber())
        .build();
  }

  public AuthenticateAccountQuery toSignInDto(AuthAccount authDomain) {
    return AuthenticateAccountQuery.builder()
        .memberUuid(authDomain.getMemberUuid())
        .memberId(authDomain.getMemberId())
        .password(authDomain.getPassword())
        .build();
  }

  public SignInResult toSignInResponseDto(
      String memberUuid, String accessToken, String refreshToken) {
    return SignInResult.builder()
        .memberUuid(memberUuid)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public StoreRefreshTokenCommand toStoreRefreshTokenDto(String memberUuid, String refreshToken) {
    return StoreRefreshTokenCommand.builder()
        .memberUuid(memberUuid)
        .refreshToken(refreshToken)
        .build();
  }

  public MemberIdLookupQuery toGetMemberIdDto(GetMemberIdQuery getMemberIdRequestDto) {
    return MemberIdLookupQuery.builder()
        .phoneNumber(getMemberIdRequestDto.getPhoneNumber())
        .build();
  }
}
