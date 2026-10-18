package com.chalnakchalnak.account.member.adapter.in.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMemberResponse {

  private String memberUuid;
  private String nickname;
  private String profileImageUrl;

  @Builder
  public ChatMemberResponse(String memberUuid, String nickname, String profileImageUrl) {
    this.memberUuid = memberUuid;
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
  }
}
