package com.chalnakchalnak.account.member.application.result;

import com.chalnakchalnak.account.member.domain.Honor;
import com.chalnakchalnak.account.member.domain.MemberProfile;
import com.chalnakchalnak.account.member.domain.State;
import com.chalnakchalnak.account.util.ProfileImageUrls;
import java.io.Serializable;
import lombok.Builder;

@lombok.Getter
public class MemberResult implements Serializable {

  private String memberUuid;
  private String nickname;
  private String gradeUuid;
  private Honor honor;
  private State state;
  private String profileImageUrl;
  private Double point;

  @Builder
  public MemberResult(
      String memberUuid,
      String nickname,
      String gradeUuid,
      Honor honor,
      State state,
      String profileImageUrl,
      Double point) {
    this.memberUuid = memberUuid;
    this.nickname = nickname;
    this.gradeUuid = gradeUuid;
    this.honor = honor;
    this.state = state;
    this.profileImageUrl = profileImageUrl;
    this.point = point;
  }

  public static MemberResult from(MemberProfile member, String bucket, String region) {
    return from(member, bucket, region, "");
  }

  public static MemberResult from(
      MemberProfile member, String bucket, String region, String publicBaseUrl) {

    String imageUrl =
        ProfileImageUrls.resolve(member.getProfileImageKey(), bucket, region, publicBaseUrl);

    return MemberResult.builder()
        .memberUuid(member.getMemberUuid())
        .nickname(member.getNickname())
        .gradeUuid(member.getGradeUuid())
        .honor(member.getHonor())
        .state(member.getState())
        .profileImageUrl(imageUrl)
        .point(member.getPoint())
        .build();
  }
}
