package com.chalnakchalnak.member_service.auth.adapter.in.web.vo.out;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponseVo {

    private String memberUuid;
    private String accessToken;
    private String refreshToken;

    @Builder
    public SignInResponseVo(String memberUuid, String accessToken, String refreshToken) {
        this.memberUuid = memberUuid;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
