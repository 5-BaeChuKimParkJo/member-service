package com.chalnakchalnak.account.common.config;

import com.chalnakchalnak.account.member.adapter.out.cache.MemberProfileCacheErrorHandler;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberCacheConfig implements CachingConfigurer {
  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    return new MemberProfileCacheErrorHandler();
  }
}
