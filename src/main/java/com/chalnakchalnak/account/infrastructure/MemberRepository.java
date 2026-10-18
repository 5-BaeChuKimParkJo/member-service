package com.chalnakchalnak.account.infrastructure;


import com.chalnakchalnak.account.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberUuid(String memberUuid);
    boolean existsByNickname(String nickname);
}
