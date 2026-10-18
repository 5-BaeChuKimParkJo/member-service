package com.chalnakchalnak.account.member.application.query;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberListQuery {
  List<String> memberUuidList;

  @Builder
  public MemberListQuery(List<String> memberUuidList) {
    this.memberUuidList = memberUuidList;
  }
}
