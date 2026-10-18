package com.chalnakchalnak.account.auth.application.query;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExistsMemberIdQuery {

  private String memberId;

  @Builder
  public ExistsMemberIdQuery(String memberId) {
    this.memberId = memberId;
  }
}
