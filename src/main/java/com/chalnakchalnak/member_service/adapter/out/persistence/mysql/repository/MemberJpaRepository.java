package com.chalnakchalnak.member_service.adapter.out.persistence.mysql.repository;

import com.chalnakchalnak.member_service.adapter.out.persistence.mysql.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByMemberUuid(String memberUuid);
    boolean existsByNickname(String nickname);
}
