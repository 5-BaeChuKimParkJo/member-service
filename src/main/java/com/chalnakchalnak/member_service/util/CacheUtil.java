package com.chalnakchalnak.member_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CacheUtil {

    private final CacheManager cacheManager;
    private final RedisTemplate redisTemplate;

    public void evictMemberCache(String value, String key) {
        if ("".equals(key)) {
            cacheManager.getCache(value).evict(SimpleKey.EMPTY);
        } else {
            cacheManager.getCache(value).evict(key);
        }
    }

    public void evictMemberCacheList(String value, String key) {
        String prefix =  value + "::";
        Set<String> keys = redisTemplate.keys(prefix + "*");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String k : keys) {
            // prefix 제거 후 ID 목록만 추출
            String idsPart = k.replace(prefix, ""); // "1,2,3,4"
            Set<String> idSet = Arrays.stream(idsPart.split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            if (idSet.contains(key)) {
                redisTemplate.delete(k);
            }
        }
    }
}
