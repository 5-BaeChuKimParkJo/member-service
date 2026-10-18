package com.chalnakchalnak.account.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResetPasswordCommand {

  private String phoneNumber;
  private String newPassword;

  @Builder
  public ResetPasswordCommand(String phoneNumber, String newPassword) {
    this.phoneNumber = phoneNumber;
    this.newPassword = newPassword;
  }
}
