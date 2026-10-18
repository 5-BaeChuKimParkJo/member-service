package com.chalnakchalnak.account.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignOutCommand {

  private String refreshToken;

  @Builder
  public SignOutCommand(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
