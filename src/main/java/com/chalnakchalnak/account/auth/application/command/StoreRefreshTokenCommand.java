package com.chalnakchalnak.account.auth.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreRefreshTokenCommand {

  private final String memberUuid;
  private final String refreshToken;

  @Builder
  public StoreRefreshTokenCommand(String memberUuid, String refreshToken) {
    this.memberUuid = memberUuid;
    this.refreshToken = refreshToken;
  }
}
