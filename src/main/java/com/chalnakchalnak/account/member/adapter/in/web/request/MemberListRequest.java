package com.chalnakchalnak.account.member.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberListRequest {

  @NotBlank(message = "멤버uuid는 필수값 입니다.")
  List<String> memberUuidList;

  @Builder
  public MemberListRequest(List<String> memberUuidList) {
    this.memberUuidList = memberUuidList;
  }
}
