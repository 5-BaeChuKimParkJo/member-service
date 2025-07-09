package com.chalnakchalnak.member_service.dto.in;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GradeEventRequestDto {

    private String memberUuid;
    private Double point;
    private String gradeUuid;

    @Builder
    public GradeEventRequestDto(String memberUuid, Double point, String gradeUuid) {
        this.memberUuid = memberUuid;
        this.point = point;
        this.gradeUuid = gradeUuid;
    }
}
