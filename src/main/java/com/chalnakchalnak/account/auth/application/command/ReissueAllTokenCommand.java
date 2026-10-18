package com.chalnakchalnak.account.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueAllTokenCommand {

  private String refreshToken;

  @Builder
  public ReissueAllTokenCommand(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
