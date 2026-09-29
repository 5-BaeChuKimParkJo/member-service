package com.chalnakchalnak.member_service.grade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeHistoryRepository extends JpaRepository<GradeHistory, Long> {

    List<GradeHistory> findByMemberUuid(String memberUuid);

    Optional<GradeHistory> findTopByMemberUuidOrderByCreatedAtDesc(String memberUuid);
}
