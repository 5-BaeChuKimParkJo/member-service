package com.chalnakchalnak.account.auth.application.query;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberIdQuery {
  private String phoneNumber;

  @Builder
  public GetMemberIdQuery(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
