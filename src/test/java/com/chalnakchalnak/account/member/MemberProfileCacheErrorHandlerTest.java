package com.chalnakchalnak.account.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.chalnakchalnak.account.common.config.MemberCacheNames;
import com.chalnakchalnak.account.member.adapter.out.cache.MemberProfileCacheErrorHandler;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cache.concurrent.ConcurrentMapCache;

@ExtendWith(OutputCaptureExtension.class)
class MemberProfileCacheErrorHandlerTest {
  private final MemberProfileCacheErrorHandler handler = new MemberProfileCacheErrorHandler();

  @ParameterizedTest
  @ValueSource(strings = {"memberList:v2", "chatMember:v2", "refresh", "unknown"})
  void propagatesBothOperationsForCachesOutsideTheProfileAllowlist(String name) {
    var cache = new ConcurrentMapCache(name);
    var failure = new IllegalStateException("fixture provider failure");
    assertThatThrownBy(() -> handler.handleCacheGetError(failure, cache, "key")).isSameAs(failure);
    assertThatThrownBy(() -> handler.handleCachePutError(failure, cache, "key", "value"))
        .isSameAs(failure);
  }

  @Test
  void neverMakesFailedInvalidationLookSuccessful() {
    var cache = new ConcurrentMapCache(MemberCacheNames.MEMBER);
    var failure = new IllegalStateException("fixture invalidation failure");
    assertThatThrownBy(() -> handler.handleCacheEvictError(failure, cache, "key"))
        .isSameAs(failure);
    assertThatThrownBy(() -> handler.handleCacheClearError(failure, cache)).isSameAs(failure);
  }

  @Test
  void logsTheOperationAndExceptionClassWithoutPrivateData(CapturedOutput output) {
    var cache = new ConcurrentMapCache(MemberCacheNames.MEMBER);
    var failure = new IllegalStateException("PRIVATE_EXCEPTION_MESSAGE");
    handler.handleCacheGetError(failure, cache, "PRIVATE_MEMBER_KEY");
    handler.handleCachePutError(
        failure,
        cache,
        "PRIVATE_MEMBER_KEY",
        Map.of("nickname", "PRIVATE_NICKNAME", "accessToken", "PRIVATE_TOKEN"));
    assertThat(output.getAll())
        .contains("cache read failed", "cache write failed", "IllegalStateException")
        .doesNotContain(
            "PRIVATE_EXCEPTION_MESSAGE", "PRIVATE_MEMBER_KEY", "PRIVATE_NICKNAME", "PRIVATE_TOKEN");
  }
}
