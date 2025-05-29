package com.chalnakchalnak.member_service.application.port.in.dto;

import com.chalnakchalnak.member_service.domain.model.State;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    private String memberUuid;
    private String nickname;

    @Builder
    public SignUpRequestDto(String memberUuid,
                            String nickname) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
    }
}
