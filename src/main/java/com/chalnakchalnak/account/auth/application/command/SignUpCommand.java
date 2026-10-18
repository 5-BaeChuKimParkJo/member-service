package com.chalnakchalnak.account.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpCommand {

  private String memberId;
  private String password;
  private String nickname;
  private String phoneNumber;

  @Builder
  public SignUpCommand(String memberId, String password, String nickname, String phoneNumber) {
    this.memberId = memberId;
    this.password = password;
    this.nickname = nickname;
    this.phoneNumber = phoneNumber;
  }
}
