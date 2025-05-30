package com.chalnakchalnak.member_service.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequestVo {

    private String memberUuid;
    private String nickname;

    @Builder
    public SignUpRequestVo(String memberUuid, String nickname) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
    }
}