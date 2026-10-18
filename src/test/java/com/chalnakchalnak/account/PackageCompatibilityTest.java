package com.chalnakchalnak.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PackageCompatibilityTest {
  @Test
  void avoidsReadingOldClassBasedCachesWithoutDeletingData() {
    assertThat(com.chalnakchalnak.account.common.config.MemberCacheNames.MEMBER)
        .isEqualTo("member:v2");
    assertThat(com.chalnakchalnak.account.common.config.MemberCacheNames.CHAT_MEMBER)
        .isEqualTo("chatMember:v2");
  }

  @Test
  void usesCanonicalAccountStartupName() throws Exception {
    Class<?> startup = Class.forName("com.chalnakchalnak.account.AccountServiceApplication");
    assertThat(
            startup.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class))
        .isTrue();
  }

  @Test
  void preservesMemberTableAndEntityIdentity() {
    Class<?> entity =
        com.chalnakchalnak.account.member.adapter.out.persistence.entity.MemberEntity.class;
    assertThat(entity.getAnnotation(jakarta.persistence.Entity.class).name()).isEqualTo("Member");
    assertThat(entity.getAnnotation(jakarta.persistence.Table.class).name()).isEqualTo("member");
  }
}
