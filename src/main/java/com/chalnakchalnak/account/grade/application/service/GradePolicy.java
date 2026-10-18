package com.chalnakchalnak.account.grade.application.service;

import com.chalnakchalnak.account.grade.application.port.out.GradeRepositoryPort;
import com.chalnakchalnak.account.grade.domain.GradeDefinition;
import com.chalnakchalnak.account.grade.domain.GradePolicyConfigurationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GradePolicy {

  private final GradeRepositoryPort gradeRepository;

  public GradeDefinition gradeFor(double points) {
    List<GradeDefinition> matches =
        gradeRepository.findAll().stream()
            .filter(grade -> grade.getMinPoint() <= points && points <= grade.getMaxPoint())
            .toList();

    if (matches.isEmpty()) {
      throw new GradePolicyConfigurationException("No grade configured for points: " + points);
    }
    if (matches.size() > 1) {
      throw new GradePolicyConfigurationException(
          "Multiple grades configured for points: " + points);
    }

    return matches.get(0);
  }
}
