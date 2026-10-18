package com.chalnakchalnak.account.member.adapter.out.persistence;

import com.chalnakchalnak.account.member.adapter.out.persistence.entity.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

  Optional<MemberEntity> findByMemberUuid(String memberUuid);

  boolean existsByNickname(String nickname);
}
