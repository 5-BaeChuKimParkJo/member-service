package com.chalnakchalnak.member_service.auth.application.port.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignOutDto {

    private String refreshToken;

    @Builder
    public SignOutDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
