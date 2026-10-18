package com.chalnakchalnak.account.auth.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthAccount {

  private String memberUuid;
  private String memberId;
  private String password;
  private String nickname;
  private String phoneNumber;

  @Builder
  public AuthAccount(
      String memberUuid, String memberId, String password, String nickname, String phoneNumber) {
    this.memberUuid = memberUuid;
    this.memberId = memberId;
    this.password = password;
    this.nickname = nickname;
    this.phoneNumber = phoneNumber;
  }
}
