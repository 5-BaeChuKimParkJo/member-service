package com.chalnakchalnak.account.grade.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GradeDefinition {
  private String gradeUuid;
  private String gradeName;
  private int minPoint;
  private int maxPoint;
  private String description;
  private int orderNumber;
  private String gradeImageKey;
}
