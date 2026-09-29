package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    private String phoneNumber;
    private String newPassword;

    @Builder
    public ResetPasswordRequestDto(String phoneNumber, String newPassword) {
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
    }
}
