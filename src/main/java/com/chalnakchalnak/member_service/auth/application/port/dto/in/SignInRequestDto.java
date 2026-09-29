package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInRequestDto {

    private String memberId;
    private String password;

    @Builder
    public SignInRequestDto(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }
}
