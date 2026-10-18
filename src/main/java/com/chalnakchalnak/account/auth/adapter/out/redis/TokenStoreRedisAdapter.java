package com.chalnakchalnak.account.auth.adapter.out.redis;

import com.chalnakchalnak.account.auth.application.command.StoreRefreshTokenCommand;
import com.chalnakchalnak.account.auth.application.port.out.TokenStorePort;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenStoreRedisAdapter implements TokenStorePort {

  private final StringRedisTemplate redisTemplate;

  private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(14);

  @Override
  public void saveRefreshToken(StoreRefreshTokenCommand storeRefreshTokenDto) {
    redisTemplate
        .opsForValue()
        .set(
            refreshTokenKey(storeRefreshTokenDto.getMemberUuid()),
            storeRefreshTokenDto.getRefreshToken(),
            REFRESH_TOKEN_TTL);
  }

  @Override
  public String getRefreshToken(String memberUuid) {
    return redisTemplate.opsForValue().get(refreshTokenKey(memberUuid));
  }

  @Override
  public void deleteRefreshToken(String memberUuid) {
    redisTemplate.delete(refreshTokenKey(memberUuid));
  }

  private String refreshTokenKey(String memberUuid) {
    return "refresh:" + memberUuid;
  }
}
