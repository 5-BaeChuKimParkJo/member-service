package com.chalnakchalnak.member_service.auth.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthDomain {

    private String memberUuid;
    private String memberId;
    private String password;
    private String nickname;
    private String phoneNumber;

    @Builder
    public AuthDomain(String memberUuid, String memberId, String password,
                      String nickname, String phoneNumber) {
        this.memberUuid = memberUuid;
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
