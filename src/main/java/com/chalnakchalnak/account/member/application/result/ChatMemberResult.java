package com.chalnakchalnak.account.member.application.result;

import com.chalnakchalnak.account.member.domain.MemberProfile;
import com.chalnakchalnak.account.util.ProfileImageUrls;
import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMemberResult implements Serializable {

  private String memberUuid;
  private String nickname;
  private String profileImageUrl;

  @Builder
  public ChatMemberResult(String memberUuid, String nickname, String profileImageUrl) {
    this.memberUuid = memberUuid;
    this.nickname = nickname;
    this.profileImageUrl = profileImageUrl;
  }

  public static ChatMemberResult from(MemberProfile member, String bucket, String region) {
    return from(member, bucket, region, "");
  }

  public static ChatMemberResult from(
      MemberProfile member, String bucket, String region, String publicBaseUrl) {
    String imageUrl =
        ProfileImageUrls.resolve(member.getProfileImageKey(), bucket, region, publicBaseUrl);
    return ChatMemberResult.builder()
        .memberUuid(member.getMemberUuid())
        .nickname(member.getNickname())
        .profileImageUrl(imageUrl)
        .build();
  }
}
