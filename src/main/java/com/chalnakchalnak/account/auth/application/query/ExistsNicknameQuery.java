package com.chalnakchalnak.account.auth.application.query;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExistsNicknameQuery {

  private String nickname;

  @Builder
  public ExistsNicknameQuery(String nickname) {
    this.nickname = nickname;
  }
}
