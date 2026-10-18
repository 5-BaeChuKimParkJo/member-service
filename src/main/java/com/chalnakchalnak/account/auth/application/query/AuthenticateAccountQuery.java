package com.chalnakchalnak.account.auth.application.query;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticateAccountQuery {

  private String memberUuid;
  private String memberId;
  private String password;

  @Builder
  public AuthenticateAccountQuery(String memberUuid, String memberId, String password) {
    this.memberUuid = memberUuid;
    this.memberId = memberId;
    this.password = password;
  }
}
