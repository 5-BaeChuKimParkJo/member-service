package com.chalnakchalnak.account.member.domain;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberProfile implements Serializable {
  private String memberUuid;
  private String nickname;
  private String gradeUuid;
  private Honor honor;
  private State state;
  private String profileImageKey;
  private Double point;
}
