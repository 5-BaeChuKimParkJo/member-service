package com.chalnakchalnak.member_service.auth.application.port.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpDto {

    private String memberUuid;
    private String memberId;
    private String password;
    private String phoneNumber;

    @Builder
    public SignUpDto(String memberUuid, String memberId, String password, String phoneNumber) {
        this.memberUuid = memberUuid;
        this.memberId = memberId;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

}
