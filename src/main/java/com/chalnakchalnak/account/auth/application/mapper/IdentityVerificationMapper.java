package com.chalnakchalnak.account.auth.application.mapper;

import com.chalnakchalnak.account.auth.application.command.VerifyCodeCommand;
import com.chalnakchalnak.account.auth.domain.model.IdentityVerification;
import org.springframework.stereotype.Component;

@Component
public class IdentityVerificationMapper {

  public IdentityVerification toIdentityVerificationDomain(VerifyCodeCommand verifyCodeRequestDto) {
    return IdentityVerification.builder()
        .phoneNumber(verifyCodeRequestDto.getPhoneNumber())
        .verificationCode(verifyCodeRequestDto.getVerificationCode())
        .purpose(verifyCodeRequestDto.getPurpose())
        .build();
  }
}
