package com.chalnakchalnak.account.member.application.port.out;

public interface MemberCachePort {
  void evictMemberCache(String cache, String memberUuid);

  void evictMemberCacheList(String cache, String memberUuid);
}
