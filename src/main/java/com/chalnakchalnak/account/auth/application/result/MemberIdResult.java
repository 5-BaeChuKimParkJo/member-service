package com.chalnakchalnak.account.auth.application.result;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberIdResult {

  private String memberId;

  @Builder
  public MemberIdResult(String memberId) {
    this.memberId = memberId;
  }
}
