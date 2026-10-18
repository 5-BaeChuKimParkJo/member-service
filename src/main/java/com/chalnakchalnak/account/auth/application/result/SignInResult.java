package com.chalnakchalnak.account.auth.application.result;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResult {

  private String memberUuid;
  private String accessToken;
  private String refreshToken;

  @Builder
  public SignInResult(String memberUuid, String accessToken, String refreshToken) {
    this.memberUuid = memberUuid;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
