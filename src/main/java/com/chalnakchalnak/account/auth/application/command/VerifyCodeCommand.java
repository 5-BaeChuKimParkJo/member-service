package com.chalnakchalnak.account.auth.application.command;

import com.chalnakchalnak.account.auth.domain.model.enums.IdentityVerificationPurpose;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VerifyCodeCommand {

  private String phoneNumber;
  private String verificationCode;
  private IdentityVerificationPurpose purpose;

  @Builder
  public VerifyCodeCommand(
      String phoneNumber, String verificationCode, IdentityVerificationPurpose purpose) {
    this.phoneNumber = phoneNumber;
    this.verificationCode = verificationCode;
    this.purpose = purpose;
  }
}
