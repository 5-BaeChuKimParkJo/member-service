package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExistsPhoneNumberRequestDto {

    private String phoneNumber;

    @Builder
    public ExistsPhoneNumberRequestDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
