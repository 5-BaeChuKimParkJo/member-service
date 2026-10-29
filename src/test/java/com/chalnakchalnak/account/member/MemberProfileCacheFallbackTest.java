package com.chalnakchalnak.account.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chalnakchalnak.account.common.config.MemberCacheNames;
import com.chalnakchalnak.account.common.exception.BaseException;
import com.chalnakchalnak.account.member.adapter.out.persistence.MemberJpaRepository;
import com.chalnakchalnak.account.member.adapter.out.persistence.entity.MemberEntity;
import com.chalnakchalnak.account.member.application.port.in.MemberUseCase;
import com.chalnakchalnak.account.member.application.result.MemberResult;
import com.chalnakchalnak.account.member.domain.State;
import java.net.SocketException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
    properties =
        "spring.datasource.url=jdbc:h2:mem:profile-cache-fallback;MODE=PostgreSQL;DB_CLOSE_DELAY=-1")
@AutoConfigureMockMvc(addFilters = false)
@Import(MemberProfileCacheFallbackTest.TestCaches.class)
class MemberProfileCacheFallbackTest {
  private static final String MEMBER_UUID = "10000000-0000-4000-8000-000000000001";

  @Autowired MockMvc mvc;
  @Autowired MemberUseCase memberService;
  @Autowired FaultyProfileCache cache;
  @SpyBean MemberJpaRepository members;

  @BeforeEach
  void seedDatabaseAndClearTestCache() {
    cache.reset();
    members.deleteAll();
    members.saveAndFlush(
        MemberEntity.builder()
            .memberUuid(MEMBER_UUID)
            .nickname("Database seller")
            .state(State.ACTIVE)
            .point(100.0)
            .build());
  }

  @ParameterizedTest
  @CsvSource({"true,false", "false,true", "true,true"})
  void returnsTheDatabaseProfileOverHttpWhenCacheReadsOrWritesFail(
      boolean readFailure, boolean writeFailure) throws Exception {
    cache.failReads = readFailure;
    cache.failWrites = writeFailure;
    mvc.perform(get("/member-service/api/v1/member/{uuid}", MEMBER_UUID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.memberUuid").value(MEMBER_UUID))
        .andExpect(jsonPath("$.nickname").value("Database seller"))
        .andExpect(jsonPath("$.state").value("ACTIVE"));
    assertThat(members.findByMemberUuid(MEMBER_UUID)).isPresent();
  }

  @Test
  void stillReturnsACacheHitInsteadOfAlwaysBypassingTheCache() throws Exception {
    cache.put(
        MEMBER_UUID,
        MemberResult.builder()
            .memberUuid(MEMBER_UUID)
            .nickname("Cached seller")
            .state(State.ACTIVE)
            .point(100.0)
            .build());
    mvc.perform(get("/member-service/api/v1/member/{uuid}", MEMBER_UUID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nickname").value("Cached seller"));
    assertThat(members.findByMemberUuid(MEMBER_UUID).orElseThrow().getNickname())
        .isEqualTo("Database seller");
  }

  @Test
  void resumesPopulationAndCacheHitsAfterTheProviderRecovers() throws Exception {
    cache.failReads = true;
    cache.failWrites = true;
    mvc.perform(get("/api/v1/member/{uuid}", MEMBER_UUID)).andExpect(status().isOk());
    assertThat(cache.getNativeCache()).isEmpty();
    cache.failReads = false;
    cache.failWrites = false;
    mvc.perform(get("/api/v1/member/{uuid}", MEMBER_UUID)).andExpect(status().isOk());
    assertThat(cache.getNativeCache()).containsKey(MEMBER_UUID);
    members.deleteAll();
    mvc.perform(get("/api/v1/member/{uuid}", MEMBER_UUID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nickname").value("Database seller"));
  }

  @Test
  void doesNotTurnAnAbsentMemberIntoAFabricatedProfileDuringACacheOutage() throws Exception {
    var baseline =
        mvc.perform(get("/api/v1/member/{uuid}", "20000000-0000-4000-8000-000000000002"))
            .andReturn()
            .getResponse();
    assertThat(baseline.getStatus()).isBetween(400, 599);
    cache.failReads = true;
    cache.failWrites = true;
    assertThatThrownBy(() -> memberService.getMember("20000000-0000-4000-8000-000000000002"))
        .isInstanceOf(BaseException.class);
    assertThat(cache.getNativeCache()).isEmpty();
    mvc.perform(get("/api/v1/member/{uuid}", "20000000-0000-4000-8000-000000000002"))
        .andExpect(status().is(baseline.getStatus()))
        .andExpect(content().json(baseline.getContentAsString()))
        .andExpect(jsonPath("$.memberUuid").doesNotExist());
  }

  @Test
  void preservesARepositoryFailureInsteadOfHidingItAsACacheFailure() {
    cache.failReads = true;
    cache.failWrites = true;
    var failure = new DataAccessResourceFailureException("fixture database failure");
    doThrow(failure).when(members).findByMemberUuid(MEMBER_UUID);
    assertThatThrownBy(() -> memberService.getMember(MEMBER_UUID)).isSameAs(failure);
    assertThat(cache.getNativeCache()).isEmpty();
  }

  @TestConfiguration(proxyBeanMethods = false)
  static class TestCaches {
    @Bean
    FaultyProfileCache profileTestCache() {
      return new FaultyProfileCache();
    }

    @Bean
    CacheManager profileTestCacheManager(FaultyProfileCache cache) {
      var manager = new SimpleCacheManager();
      manager.setCaches(List.of(cache));
      return manager;
    }
  }

  static class FaultyProfileCache extends ConcurrentMapCache {
    boolean failReads;
    boolean failWrites;

    FaultyProfileCache() {
      super(MemberCacheNames.MEMBER);
    }

    @Override
    public ValueWrapper get(Object key) {
      if (failReads)
        throw new RedisSystemException(
            "fixture read failure", new SocketException("Connection reset"));
      return super.get(key);
    }

    @Override
    public void put(Object key, Object value) {
      if (failWrites)
        throw new RedisSystemException(
            "fixture write failure", new SocketException("Connection reset"));
      super.put(key, value);
    }

    void reset() {
      failReads = false;
      failWrites = false;
      super.clear();
    }
  }
}
