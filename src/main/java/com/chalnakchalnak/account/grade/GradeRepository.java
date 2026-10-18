package com.chalnakchalnak.account.grade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByGradeUuid(String gradeUuid);

    Optional<Grade> findByOrderNumber(int orderNumber);
}
