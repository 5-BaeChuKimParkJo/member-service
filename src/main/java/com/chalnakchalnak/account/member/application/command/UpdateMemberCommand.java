package com.chalnakchalnak.account.member.application.command;

import com.chalnakchalnak.account.member.domain.Honor;
import com.chalnakchalnak.account.member.domain.State;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateMemberCommand {

  private String memberUuid;
  private String nickname;
  private String gradeUuid;
  private Honor honor;
  private State state;
  private String profileImageKey;
  private Double point;

  @Builder
  public UpdateMemberCommand(
      String memberUuid,
      String nickname,
      String gradeUuid,
      Honor honor,
      State state,
      String profileImageKey,
      Double point) {
    this.memberUuid = memberUuid;
    this.nickname = nickname;
    this.gradeUuid = gradeUuid;
    this.honor = honor;
    this.state = state;
    this.profileImageKey = profileImageKey;
    this.point = point;
  }
}
