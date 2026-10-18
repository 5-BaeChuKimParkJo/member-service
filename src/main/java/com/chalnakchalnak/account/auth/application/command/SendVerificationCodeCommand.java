package com.chalnakchalnak.account.auth.application.command;

import com.chalnakchalnak.account.auth.domain.model.enums.IdentityVerificationPurpose;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SendVerificationCodeCommand {

  private String phoneNumber;
  private IdentityVerificationPurpose purpose;

  @Builder
  public SendVerificationCodeCommand(String phoneNumber, IdentityVerificationPurpose purpose) {
    this.phoneNumber = phoneNumber;
    this.purpose = purpose;
  }
}
