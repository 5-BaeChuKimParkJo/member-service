package com.chalnakchalnak.account.auth.application.command;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreAuthCommand {

  private String memberUuid;
  private String memberId;
  private String password;
  private String phoneNumber;

  @Builder
  public StoreAuthCommand(String memberUuid, String memberId, String password, String phoneNumber) {
    this.memberUuid = memberUuid;
    this.memberId = memberId;
    this.password = password;
    this.phoneNumber = phoneNumber;
  }
}
