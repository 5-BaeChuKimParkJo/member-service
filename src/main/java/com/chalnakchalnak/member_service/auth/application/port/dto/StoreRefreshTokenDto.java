package com.chalnakchalnak.member_service.auth.application.port.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreRefreshTokenDto {

    private final String memberUuid;
    private final String refreshToken;

    @Builder
    public StoreRefreshTokenDto(String memberUuid, String refreshToken) {
        this.memberUuid = memberUuid;
        this.refreshToken = refreshToken;
    }
}
