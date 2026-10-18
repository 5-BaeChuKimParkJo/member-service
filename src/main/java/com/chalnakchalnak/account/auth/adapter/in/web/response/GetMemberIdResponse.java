package com.chalnakchalnak.account.auth.adapter.in.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberIdResponse {

  private String memberId;

  @Builder
  public GetMemberIdResponse(String memberId) {
    this.memberId = memberId;
  }
}
