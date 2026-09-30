package com.chalnakchalnak.member_service.account;

import com.chalnakchalnak.member_service.grade.Grade;
import com.chalnakchalnak.member_service.grade.GradePolicy;
import com.chalnakchalnak.member_service.grade.GradePolicyConfigurationException;
import com.chalnakchalnak.member_service.grade.GradeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GradePolicyTest {

    private final GradeRepository repository = mock(GradeRepository.class);
    private final GradePolicy policy = new GradePolicy(repository);

    @Test
    void selectsConfiguredOrderFiveGradeAtInitialPoints() {
        when(repository.findAll()).thenReturn(List.of(
                grade("default-grade", 5, 100, 199)
        ));

        assertThat(policy.gradeFor(100.0).getOrderNumber()).isEqualTo(5);
    }

    @Test
    void includesMinimumAndMaximumBoundaries() {
        Grade grade = grade("bounded", 5, 100, 200);
        when(repository.findAll()).thenReturn(List.of(grade));

        assertThat(policy.gradeFor(100.0).getGradeUuid()).isEqualTo("bounded");
        assertThat(policy.gradeFor(200.0).getGradeUuid()).isEqualTo("bounded");
    }

    @Test
    void reportsGapAtRequestedPoint() {
        when(repository.findAll()).thenReturn(List.of(
                grade("lower", 4, 0, 99),
                grade("upper", 5, 101, 200)
        ));

        assertThatThrownBy(() -> policy.gradeFor(100.0))
                .isInstanceOf(GradePolicyConfigurationException.class)
                .hasMessageContaining("No grade");
    }

    @Test
    void reportsOverlapAtRequestedPoint() {
        when(repository.findAll()).thenReturn(List.of(
                grade("lower", 4, 0, 100),
                grade("upper", 5, 100, 200)
        ));

        assertThatThrownBy(() -> policy.gradeFor(100.0))
                .isInstanceOf(GradePolicyConfigurationException.class)
                .hasMessageContaining("Multiple grades");
    }

    private Grade grade(String uuid, int order, int min, int max) {
        return Grade.builder()
                .gradeUuid(uuid)
                .gradeName(uuid)
                .orderNumber(order)
                .minPoint(min)
                .maxPoint(max)
                .build();
    }
}
