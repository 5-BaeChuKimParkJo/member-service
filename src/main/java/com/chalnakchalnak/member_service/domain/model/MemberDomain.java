package com.chalnakchalnak.member_service.domain.model;

import com.chalnakchalnak.member_service.adapter.out.persistence.mysql.entity.MemberEntity;
import com.chalnakchalnak.member_service.application.mapper.feign.MemberMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDomain {

    private String memberUuid;
    private String nickname;
    private String gradeName;
    private String honor_name;
    private State state;
    private String profileImageUrl;

    @Builder
    public MemberDomain(String memberUuid,
                        String nickname,
                        String gradeName,
                        String honor_name,
                        State state,
                        String profileImageUrl) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
        this.gradeName = gradeName;
        this.honor_name = honor_name;
        this.state = state;
        this.profileImageUrl = profileImageUrl;
    }
}
