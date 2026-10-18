package com.chalnakchalnak.account.common.config;

public final class MemberCacheNames {
  // Class-based cache values from the old package expire without being deserialized.
  public static final String MEMBER = "member:v2";
  public static final String MEMBER_LIST = "memberList:v2";
  public static final String CHAT_MEMBER = "chatMember:v2";

  private MemberCacheNames() {}
}
