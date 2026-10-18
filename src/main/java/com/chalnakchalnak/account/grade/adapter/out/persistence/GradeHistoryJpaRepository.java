package com.chalnakchalnak.account.grade.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeHistoryJpaRepository extends JpaRepository<GradeHistoryEntity, Long> {

  List<GradeHistoryEntity> findByMemberUuid(String memberUuid);

  Optional<GradeHistoryEntity> findTopByMemberUuidOrderByCreatedAtDesc(String memberUuid);
}
