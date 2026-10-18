package com.chalnakchalnak.account.member.adapter.in.web.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterMemberRequest {

  private String memberUuid;
  private String nickname;

  @Builder
  public RegisterMemberRequest(String memberUuid, String nickname) {
    this.memberUuid = memberUuid;
    this.nickname = nickname;
  }
}
