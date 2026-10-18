package com.chalnakchalnak.account.grade.application.port.out;

import com.chalnakchalnak.account.grade.domain.GradeDefinition;
import java.util.List;
import java.util.Optional;

public interface GradeRepositoryPort {
  List<GradeDefinition> findAll();

  Optional<GradeDefinition> findByOrderNumber(int order);
}
