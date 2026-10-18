package com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.repository;

import com.chalnakchalnak.account.auth.adapter.out.persistence.postgresql.entity.AuthEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthJpaRepository extends JpaRepository<AuthEntity, String> {

  Boolean existsByMemberId(String memberId);

  Boolean existsByPhoneNumber(String phoneNumber);

  Optional<AuthEntity> findByMemberId(String memberId);

  Optional<AuthEntity> findMemberIdByPhoneNumber(String phoneNumber);

  Optional<AuthEntity> findByPhoneNumber(String phoneNumber);
}
