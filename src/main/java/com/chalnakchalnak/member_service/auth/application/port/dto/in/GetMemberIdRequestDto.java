package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberIdRequestDto {
    private String phoneNumber;

    @Builder
    public GetMemberIdRequestDto(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
