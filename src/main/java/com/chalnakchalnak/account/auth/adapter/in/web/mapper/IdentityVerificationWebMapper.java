package com.chalnakchalnak.account.auth.adapter.in.web.mapper;

import com.chalnakchalnak.account.auth.adapter.in.web.request.SendVerificationCodeRequest;
import com.chalnakchalnak.account.auth.adapter.in.web.request.VerifyCodeRequest;
import com.chalnakchalnak.account.auth.application.command.SendVerificationCodeCommand;
import com.chalnakchalnak.account.auth.application.command.VerifyCodeCommand;
import org.springframework.stereotype.Component;

@Component
public class IdentityVerificationWebMapper {

  public SendVerificationCodeCommand toSendVerificationCodeDto(
      SendVerificationCodeRequest sendVerificationCodeRequestVo) {
    return SendVerificationCodeCommand.builder()
        .phoneNumber(sendVerificationCodeRequestVo.getPhoneNumber())
        .purpose(sendVerificationCodeRequestVo.getPurpose())
        .build();
  }

  public VerifyCodeCommand toVerifyCodeDto(VerifyCodeRequest verifyCodeRequestVo) {
    return VerifyCodeCommand.builder()
        .phoneNumber(verifyCodeRequestVo.getPhoneNumber())
        .verificationCode(verifyCodeRequestVo.getVerificationCode())
        .purpose(verifyCodeRequestVo.getPurpose())
        .build();
  }
}
