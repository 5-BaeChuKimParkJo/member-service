package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SignUpRequestDto {

    private String memberId;
    private String password;
    private String nickname;
    private String phoneNumber;

    @Builder
    public SignUpRequestDto(String memberId, String password, String nickname, String phoneNumber) {
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
