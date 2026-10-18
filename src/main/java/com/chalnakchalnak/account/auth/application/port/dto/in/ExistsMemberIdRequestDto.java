package com.chalnakchalnak.account.auth.application.port.dto.in;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExistsMemberIdRequestDto {

    private String memberId;

    @Builder
    public ExistsMemberIdRequestDto(String memberId) {
        this.memberId = memberId;
    }
}
