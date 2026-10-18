package com.chalnakchalnak.account.auth.application.query;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdLookupQuery {

  private String phoneNumber;

  @Builder
  public MemberIdLookupQuery(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
