package com.chalnakchalnak.member_service.grade;

import com.chalnakchalnak.member_service.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grade_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GradeHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_uuid", nullable = false)
    private String memberUuid;

    @Column(name = "post_uuid", nullable = false)
    private String postUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type", nullable = false)
    private PostType postType;

    @Column(name = "point", nullable = false)
    private Double point;

    @Column(name = "total_point", nullable = false)
    private Double totalPoint;

    @Column(name = "grade_uuid", nullable = false)
    private String gradeUuid;

    @Builder
    public GradeHistory(String memberUuid, String postUuid, PostType postType,
                        Double point, Double totalPoint, String gradeUuid) {
        this.memberUuid = memberUuid;
        this.postUuid = postUuid;
        this.postType = postType;
        this.point = point;
        this.totalPoint = totalPoint;
        this.gradeUuid = gradeUuid;
    }
}
