package com.chalnakchalnak.member_service.grade;

import com.chalnakchalnak.member_service.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grade")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gradeId;

    @Column(name = "grade_uuid", nullable = false, unique = true)
    private String gradeUuid;

    @Column(name = "grade_name", nullable = false)
    private String gradeName;

    @Column(name = "min_point", nullable = false)
    private int minPoint;

    @Column(name = "max_point", nullable = false)
    private int maxPoint;

    @Column(name = "description")
    private String description;

    @Column(name = "order_number", nullable = false, unique = true)
    private int orderNumber;

    @Column(name = "grade_image_key")
    private String gradeImageKey;

    @Builder
    public Grade(String gradeUuid, String gradeName, int minPoint, int maxPoint,
                 String description, int orderNumber, String gradeImageKey) {
        this.gradeUuid = gradeUuid;
        this.gradeName = gradeName;
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.description = description;
        this.orderNumber = orderNumber;
        this.gradeImageKey = gradeImageKey;
    }
}
