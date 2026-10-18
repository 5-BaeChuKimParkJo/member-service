package com.chalnakchalnak.account.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.chalnakchalnak.account.grade.application.port.out.GradeRepositoryPort;
import com.chalnakchalnak.account.grade.application.service.GradePolicy;
import com.chalnakchalnak.account.grade.domain.GradeDefinition;
import com.chalnakchalnak.account.grade.domain.GradePolicyConfigurationException;
import java.util.List;
import org.junit.jupiter.api.Test;

class GradePolicyTest {

  private final GradeRepositoryPort repository = mock(GradeRepositoryPort.class);
  private final GradePolicy policy = new GradePolicy(repository);

  @Test
  void selectsConfiguredOrderFiveGradeAtInitialPoints() {
    when(repository.findAll()).thenReturn(List.of(grade("default-grade", 5, 100, 199)));

    assertThat(policy.gradeFor(100.0).getOrderNumber()).isEqualTo(5);
  }

  @Test
  void includesMinimumAndMaximumBoundaries() {
    GradeDefinition grade = grade("bounded", 5, 100, 200);
    when(repository.findAll()).thenReturn(List.of(grade));

    assertThat(policy.gradeFor(100.0).getGradeUuid()).isEqualTo("bounded");
    assertThat(policy.gradeFor(200.0).getGradeUuid()).isEqualTo("bounded");
  }

  @Test
  void reportsGapAtRequestedPoint() {
    when(repository.findAll())
        .thenReturn(List.of(grade("lower", 4, 0, 99), grade("upper", 5, 101, 200)));

    assertThatThrownBy(() -> policy.gradeFor(100.0))
        .isInstanceOf(GradePolicyConfigurationException.class)
        .hasMessageContaining("No grade");
  }

  @Test
  void reportsOverlapAtRequestedPoint() {
    when(repository.findAll())
        .thenReturn(List.of(grade("lower", 4, 0, 100), grade("upper", 5, 100, 200)));

    assertThatThrownBy(() -> policy.gradeFor(100.0))
        .isInstanceOf(GradePolicyConfigurationException.class)
        .hasMessageContaining("Multiple grades");
  }

  private GradeDefinition grade(String uuid, int order, int min, int max) {
    return GradeDefinition.builder()
        .gradeUuid(uuid)
        .gradeName(uuid)
        .orderNumber(order)
        .minPoint(min)
        .maxPoint(max)
        .build();
  }
}
