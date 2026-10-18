package com.chalnakchalnak.account.auth.adapter.in.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {

  private String memberUuid;
  private String accessToken;
  private String refreshToken;

  @Builder
  public SignInResponse(String memberUuid, String accessToken, String refreshToken) {
    this.memberUuid = memberUuid;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
