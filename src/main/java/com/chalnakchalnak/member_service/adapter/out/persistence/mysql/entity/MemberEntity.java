package com.chalnakchalnak.member_service.adapter.out.persistence.mysql.entity;

import com.chalnakchalnak.member_service.domain.model.State;
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
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "member_uuid", unique = true, nullable = false, length = 50)
    private String memberUuid;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "grade_name")
    private String gradeName;

    @Column(name = "honor_name")
    private String honorName;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Builder
    public MemberEntity(Long id,
                        String memberUuid,
                        String nickname,
                        String gradeName,
                        String honorName,
                        State state,
                        String profileImageUrl) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeName = gradeName;
        this.honorName = honorName;
        this.state = state;
        this.profileImageUrl = profileImageUrl;
    }
}
