package com.chalnakchalnak.member_service.auth.application.port.dto.feign.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateMemberRequestDto {

    private String memberUuid;
    private String nickname;

    @Builder
    public CreateMemberRequestDto(String memberUuid, String nickname) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
    }
}
