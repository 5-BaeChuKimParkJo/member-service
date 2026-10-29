package com.chalnakchalnak.account.member.adapter.out.cache;

import com.chalnakchalnak.account.common.config.MemberCacheNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;

public class MemberProfileCacheErrorHandler extends SimpleCacheErrorHandler {
  private static final Logger log = LoggerFactory.getLogger(MemberProfileCacheErrorHandler.class);

  @Override
  public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
    if (!MemberCacheNames.MEMBER.equals(cache.getName())) {
      super.handleCacheGetError(exception, cache, key);
      return;
    }
    warn("read", exception);
  }

  @Override
  public void handleCachePutError(
      RuntimeException exception, Cache cache, Object key, Object value) {
    if (!MemberCacheNames.MEMBER.equals(cache.getName())) {
      super.handleCachePutError(exception, cache, key, value);
      return;
    }
    warn("write", exception);
  }

  private static void warn(String operation, RuntimeException exception) {
    log.warn("member:v2 cache {} failed ({})", operation, exception.getClass().getSimpleName());
  }
}
