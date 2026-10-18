package com.chalnakchalnak.account.member.adapter.in.web;

import com.chalnakchalnak.account.member.adapter.in.web.response.ChatMemberResponse;
import com.chalnakchalnak.account.member.adapter.in.web.response.MemberResponse;
import com.chalnakchalnak.account.member.application.result.ChatMemberResult;
import com.chalnakchalnak.account.member.application.result.MemberResult;

public final class MemberWebMapper {
  private MemberWebMapper() {}

  public static MemberResponse toResponse(MemberResult result) {
    return MemberResponse.builder()
        .memberUuid(result.getMemberUuid())
        .nickname(result.getNickname())
        .gradeUuid(result.getGradeUuid())
        .honor(result.getHonor())
        .state(result.getState())
        .profileImageUrl(result.getProfileImageUrl())
        .point(result.getPoint())
        .build();
  }

  public static ChatMemberResponse toResponse(ChatMemberResult result) {
    return ChatMemberResponse.builder()
        .memberUuid(result.getMemberUuid())
        .nickname(result.getNickname())
        .profileImageUrl(result.getProfileImageUrl())
        .build();
  }
}
