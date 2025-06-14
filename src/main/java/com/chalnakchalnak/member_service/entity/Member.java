package com.chalnakchalnak.member_service.entity;

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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "member_uuid", unique = true, nullable = false, length = 50)
    private String memberUuid;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "grade_uuid")
    private String gradeUuid;

    @Column(name = "honor")
    @Enumerated(EnumType.STRING)
    private Honor honor;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "point", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long point;

    @Builder
    public Member(Long id,
                        String memberUuid,
                        String nickname,
                        String gradeUuid,
                        Honor honor,
                        State state,
                        String profileImageUrl,
                        Long point) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeUuid = gradeUuid;
        this.honor = honor;
        this.state = state;
        this.profileImageUrl = profileImageUrl;
        this.point = point;
    }

    public void setProfileImageUrl(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }
}
