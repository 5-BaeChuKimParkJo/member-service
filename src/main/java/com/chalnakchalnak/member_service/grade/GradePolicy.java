package com.chalnakchalnak.member_service.grade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GradePolicy {

    private final GradeRepository gradeRepository;

    public Grade gradeFor(double points) {
        List<Grade> matches = gradeRepository.findAll().stream()
                .filter(grade -> grade.getMinPoint() <= points && points <= grade.getMaxPoint())
                .toList();

        if (matches.isEmpty()) {
            throw new GradePolicyConfigurationException("No grade configured for points: " + points);
        }
        if (matches.size() > 1) {
            throw new GradePolicyConfigurationException("Multiple grades configured for points: " + points);
        }

        return matches.get(0);
    }
}
