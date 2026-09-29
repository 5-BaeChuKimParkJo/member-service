package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExistsNicknameRequestDto {

    private String nickname;

    @Builder
    public ExistsNicknameRequestDto(String nickname) {
        this.nickname = nickname;
    }
}
