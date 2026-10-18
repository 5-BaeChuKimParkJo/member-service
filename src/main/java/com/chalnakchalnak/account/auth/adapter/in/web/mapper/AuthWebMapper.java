package com.chalnakchalnak.account.auth.adapter.in.web.mapper;

import com.chalnakchalnak.account.auth.adapter.in.web.request.*;
import com.chalnakchalnak.account.auth.adapter.in.web.request.ExistsMemberIdRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.ExistsNicknameRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.ExistsPhoneNumberRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.GetMemberIdRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.ResetPasswordRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.SignInRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.SignUpRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.response.GetMemberIdResponse;
import com.chalnakchalnak.account.auth.adapter.in.web.response.SignInResponse;
import com.chalnakchalnak.account.auth.application.command.ReissueAllTokenCommand;
import com.chalnakchalnak.account.auth.application.command.ResetPasswordCommand;
import com.chalnakchalnak.account.auth.application.command.SignInCommand;
import com.chalnakchalnak.account.auth.application.command.SignOutCommand;
import com.chalnakchalnak.account.auth.application.command.SignUpCommand;
import com.chalnakchalnak.account.auth.application.query.ExistsMemberIdQuery;
import com.chalnakchalnak.account.auth.application.query.ExistsNicknameQuery;
import com.chalnakchalnak.account.auth.application.query.ExistsPhoneNumberQuery;
import com.chalnakchalnak.account.auth.application.query.GetMemberIdQuery;
import com.chalnakchalnak.account.auth.application.result.MemberIdResult;
import com.chalnakchalnak.account.auth.application.result.SignInResult;
import org.springframework.stereotype.Component;

@Component
public class AuthWebMapper {

  public SignUpCommand toSignUpRequestDto(SignUpRequest signUpRequestVo) {
    return SignUpCommand.builder()
        .memberId(signUpRequestVo.getMemberId())
        .password(signUpRequestVo.getPassword())
        .nickname(signUpRequestVo.getNickname())
        .phoneNumber(signUpRequestVo.getPhoneNumber())
        .build();
  }

  public ExistsMemberIdQuery toExistsMemberIdRequestDto(
      ExistsMemberIdRequest existsMemberIdRequestVo) {
    return ExistsMemberIdQuery.builder().memberId(existsMemberIdRequestVo.getMemberId()).build();
  }

  public ExistsNicknameQuery toExistsNicknameRequestDto(
      ExistsNicknameRequest existsNicknameRequestVo) {
    return ExistsNicknameQuery.builder().nickname(existsNicknameRequestVo.getNickname()).build();
  }

  public ExistsPhoneNumberQuery toExistsPhoneNumberRequestDto(
      ExistsPhoneNumberRequest existsPhoneNumberRequestVo) {
    return ExistsPhoneNumberQuery.builder()
        .phoneNumber(existsPhoneNumberRequestVo.getPhoneNumber())
        .build();
  }

  public SignInCommand toSignInRequestDto(SignInRequest signInRequestVo) {
    return SignInCommand.builder()
        .memberId(signInRequestVo.getMemberId())
        .password(signInRequestVo.getPassword())
        .build();
  }

  public SignInResponse toSignInResponseVo(SignInResult signInResponseDto) {
    return SignInResponse.builder()
        .memberUuid(signInResponseDto.getMemberUuid())
        .accessToken(signInResponseDto.getAccessToken())
        .refreshToken(signInResponseDto.getRefreshToken())
        .build();
  }

  public ReissueAllTokenCommand toReissueAllTokenRequestDto(String refreshToken) {
    return ReissueAllTokenCommand.builder().refreshToken(refreshToken).build();
  }

  public SignOutCommand toSignOutDto(String refreshToken) {
    return SignOutCommand.builder().refreshToken(refreshToken).build();
  }

  public GetMemberIdQuery toGetMemberIdRequestDto(GetMemberIdRequest getMemberIdRequestVo) {
    return GetMemberIdQuery.builder().phoneNumber(getMemberIdRequestVo.getPhoneNumber()).build();
  }

  public GetMemberIdResponse toGetMemberIdResponseVo(MemberIdResult getMemberIdResponseDto) {
    return GetMemberIdResponse.builder().memberId(getMemberIdResponseDto.getMemberId()).build();
  }

  public ResetPasswordCommand toResetPasswordRequestDto(
      ResetPasswordRequest resetPasswordRequestVo) {
    return ResetPasswordCommand.builder()
        .phoneNumber(resetPasswordRequestVo.getPhoneNumber())
        .newPassword(resetPasswordRequestVo.getNewPassword())
        .build();
  }
}
