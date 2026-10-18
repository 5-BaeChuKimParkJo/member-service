package com.chalnakchalnak.account.member.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class RegisterMemberCommand {

  private String memberUuid;
  private String nickname;
  private String defaultProfileImageKey =
      "member/temp_member_uuid/images/91a21dc1-0b39-4238-9a4f-5e18de8f48bd.png";

  @Builder
  public RegisterMemberCommand(String memberUuid, String nickname) {
    this.memberUuid = memberUuid;
    this.nickname = nickname;
  }
}
