package com.chalnakchalnak.member_service.entity;

import com.chalnakchalnak.member_service.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "member_uuid", unique = true, nullable = false, length = 50)
    private String memberUuid;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "grade_uuid")
    private String gradeUuid;

    @Column(name = "honor")
    @Enumerated(EnumType.STRING)
    private Honor honor;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "profile_image_key")
    private String profileImageKey;

    @Column(name = "point", nullable = false)
    private Double point;

    @Builder
    public Member(Long id,
                        String memberUuid,
                        String nickname,
                        String gradeUuid,
                        Honor honor,
                        State state,
                        String profileImageKey,
                        Double point) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeUuid = gradeUuid;
        this.honor = honor;
        this.state = state;
        this.profileImageKey = profileImageKey;
        this.point = point;
    }

    public void setProfileImageKey(String profileImageKey) {
        this.profileImageKey = profileImageKey;
    }
}
