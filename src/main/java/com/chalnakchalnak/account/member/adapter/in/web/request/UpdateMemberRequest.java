package com.chalnakchalnak.account.member.adapter.in.web.request;

import com.chalnakchalnak.account.member.domain.Honor;
import com.chalnakchalnak.account.member.domain.State;
import lombok.Getter;

@Getter
public class UpdateMemberRequest {
  private String nickname;
  private String gradeUuid;
  private Honor honor;
  private State state;
  private String profileImageKey;
  private Double point;
}
