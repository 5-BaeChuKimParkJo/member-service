package com.chalnakchalnak.account.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInCommand {

  private String memberId;
  private String password;

  @Builder
  public SignInCommand(String memberId, String password) {
    this.memberId = memberId;
    this.password = password;
  }
}
