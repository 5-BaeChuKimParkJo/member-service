package com.chalnakchalnak.account.profile.application.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SaveProfileImageCommand {

  private String profileImageKey;
  private String memberUuid;

  @Builder
  public SaveProfileImageCommand(String profileImageKey, String memberUuid) {
    this.profileImageKey = profileImageKey;
    this.memberUuid = memberUuid;
  }
}
