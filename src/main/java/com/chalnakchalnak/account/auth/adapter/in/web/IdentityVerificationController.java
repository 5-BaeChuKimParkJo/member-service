package com.chalnakchalnak.account.auth.adapter.in.web;

import com.chalnakchalnak.account.auth.adapter.in.web.mapper.IdentityVerificationWebMapper;
import com.chalnakchalnak.account.auth.adapter.in.web.request.SendVerificationCodeRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.VerifyCodeRequest;
import com.chalnakchalnak.account.auth.application.port.in.IdentityVerificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({
  "/api/v1/identity-verification",
  "/auth-service/api/v1/identity-verification",
  "/account-service/api/v1/identity-verification"
})
@RequiredArgsConstructor
public class IdentityVerificationController {

  private final IdentityVerificationUseCase identityVerificationUseCase;
  private final IdentityVerificationWebMapper identityVerificationVoMapper;

  @Operation(
      summary = "Send Verification Code API",
      description = "인증 코드 전송 API",
      tags = {"identity-verification"})
  @PostMapping("/sms/send")
  public void sendVerificationCode(
      @RequestBody @Valid SendVerificationCodeRequest sendVerificationCodeRequestVo) {

    identityVerificationUseCase.sendVerificationCode(
        identityVerificationVoMapper.toSendVerificationCodeDto(sendVerificationCodeRequestVo));
  }

  @Operation(
      summary = "Verify Code API",
      description = "인증 코드 검증 API",
      tags = {"identity-verification"})
  @PostMapping("/sms/verify")
  public Boolean verifyCode(@RequestBody @Valid VerifyCodeRequest verifyCodeRequestVo) {

    return identityVerificationUseCase.verifyCode(
        identityVerificationVoMapper.toVerifyCodeDto(verifyCodeRequestVo));
  }
}
