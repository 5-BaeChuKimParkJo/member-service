package com.chalnakchalnak.member_service.auth.application.port.dto.out;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponseDto {

    private String memberUuid;
    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResponseDto(String memberUuid, String accessToken, String refreshToken) {
        this.memberUuid = memberUuid;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
