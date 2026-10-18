package com.chalnakchalnak.account.grade.adapter.out.persistence;

import com.chalnakchalnak.account.grade.application.port.out.GradeRepositoryPort;
import com.chalnakchalnak.account.grade.domain.GradeDefinition;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GradeJpaAdapter implements GradeRepositoryPort {
  private final GradeJpaRepository repository;

  @Override
  public List<GradeDefinition> findAll() {
    return repository.findAll().stream().map(this::toDefinition).toList();
  }

  @Override
  public Optional<GradeDefinition> findByOrderNumber(int order) {
    return repository.findByOrderNumber(order).map(this::toDefinition);
  }

  private GradeDefinition toDefinition(GradeEntity grade) {
    return GradeDefinition.builder()
        .gradeUuid(grade.getGradeUuid())
        .gradeName(grade.getGradeName())
        .minPoint(grade.getMinPoint())
        .maxPoint(grade.getMaxPoint())
        .orderNumber(grade.getOrderNumber())
        .description(grade.getDescription())
        .gradeImageKey(grade.getGradeImageKey())
        .build();
  }
}
