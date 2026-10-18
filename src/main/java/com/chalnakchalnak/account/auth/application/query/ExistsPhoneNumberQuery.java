package com.chalnakchalnak.account.auth.application.query;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExistsPhoneNumberQuery {

  private String phoneNumber;

  @Builder
  public ExistsPhoneNumberQuery(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
