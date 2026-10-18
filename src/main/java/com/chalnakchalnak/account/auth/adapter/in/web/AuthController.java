package com.chalnakchalnak.account.auth.adapter.in.web;

import com.chalnakchalnak.account.auth.adapter.in.web.mapper.AuthWebMapper;
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
import com.chalnakchalnak.account.auth.application.port.in.AuthUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/v1/auth", "/auth-service/api/v1/auth", "/account-service/api/v1/auth"})
@RequiredArgsConstructor
public class AuthController {

  private final AuthUseCase authUseCase;
  private final AuthWebMapper authVoMapper;

  @Operation(
      summary = "Sign Up API",
      description = "회원가입",
      tags = {"auth"})
  @PostMapping("/sign-up")
  public void signUp(@RequestBody @Valid SignUpRequest signUpRequestVo) {

    authUseCase.signUp(authVoMapper.toSignUpRequestDto(signUpRequestVo));
  }

  @Operation(
      summary = "Check Member ID API",
      description = "회원 아이디 중복 확인",
      tags = {"auth"})
  @GetMapping("/exists/member-id")
  public Boolean existsMemberId(
      @ModelAttribute @Valid ExistsMemberIdRequest existsMemberIdRequestVo) {

    return authUseCase.existsMemberId(
        authVoMapper.toExistsMemberIdRequestDto(existsMemberIdRequestVo));
  }

  @Operation(
      summary = "Check Nickname API",
      description = "닉네임 중복 확인",
      tags = {"auth"})
  @GetMapping("/exists/nickname")
  public Boolean existsNickname(
      @ModelAttribute @Valid ExistsNicknameRequest existsNicknameRequestVo) {

    return authUseCase.existsNickname(
        authVoMapper.toExistsNicknameRequestDto(existsNicknameRequestVo));
  }

  @Operation(
      summary = "Check PhoneNumber API",
      description = "전화번호 중복 확인",
      tags = {"auth"})
  @GetMapping("/exists/phone-number")
  public Boolean existsPhoneNumber(
      @ModelAttribute @Valid ExistsPhoneNumberRequest existsMemberIdRequestVo) {

    return authUseCase.existsPhoneNumber(
        authVoMapper.toExistsPhoneNumberRequestDto(existsMemberIdRequestVo));
  }

  @Operation(
      summary = "Sign In API",
      description = "로그인",
      tags = {"auth"})
  @PostMapping("/sign-in")
  public SignInResponse signIn(@RequestBody @Valid SignInRequest signInRequestVo) {

    return authVoMapper.toSignInResponseVo(
        authUseCase.signIn(authVoMapper.toSignInRequestDto(signInRequestVo)));
  }

  @Operation(
      summary = "Reissue Token API",
      description = "토큰 재발급",
      tags = {"auth"})
  @GetMapping("/reissue")
  public SignInResponse reissueAllToken(@RequestHeader("Authorization") String refreshToken) {

    return authVoMapper.toSignInResponseVo(
        authUseCase.reissueAllToken(
            authVoMapper.toReissueAllTokenRequestDto(refreshToken.substring(7))));
  }

  @Operation(
      summary = "Sign Out API",
      description = "로그아웃",
      tags = {"auth"})
  @GetMapping("/sign-out")
  public void signOut(@RequestHeader("Authorization") String refreshToken) {

    authUseCase.signOut(authVoMapper.toSignOutDto(refreshToken.substring(7)));
  }

  @Operation(
      summary = "Find Member ID API",
      description = "회원 아이디 찾기",
      tags = {"auth"})
  @GetMapping("/member-id")
  public GetMemberIdResponse findMemberId(
      @ModelAttribute @Valid GetMemberIdRequest getMemberIdRequestVo) {

    return authVoMapper.toGetMemberIdResponseVo(
        authUseCase.getMemberId(authVoMapper.toGetMemberIdRequestDto(getMemberIdRequestVo)));
  }

  @Operation(
      summary = "Reest Password API",
      description = "비밀번호 재설정",
      tags = {"auth"})
  @PatchMapping("/password")
  public void resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequestVo) {

    authUseCase.resetPassword(authVoMapper.toResetPasswordRequestDto(resetPasswordRequestVo));
  }
}
