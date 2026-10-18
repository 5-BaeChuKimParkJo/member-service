package com.chalnakchalnak.account.grade.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeJpaRepository extends JpaRepository<GradeEntity, Long> {

  Optional<GradeEntity> findByGradeUuid(String gradeUuid);

  Optional<GradeEntity> findByOrderNumber(int orderNumber);
}
