package com.chalnakchalnak.member_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class CacheUtil {

    private final CacheManager cacheManager;
    private final RedisTemplate redisTemplate;

    public void evictMemberCache(String value, String key) {
        cacheManager.getCache(value).evict(key);
    }

    public void evictMemberCacheList(String value, String key) {
        Set<String> keys = redisTemplate.keys(value + "::*" + key + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
