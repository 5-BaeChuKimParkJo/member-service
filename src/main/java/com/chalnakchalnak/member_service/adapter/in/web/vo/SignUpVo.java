package com.chalnakchalnak.member_service.adapter.in.web.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpVo {

    private String memberUuid;
    private String nickname;

    @Builder
    public SignUpVo(String memberUuid, String nickname) {
        this.memberUuid = memberUuid;
        this.nickname = nickname;
    }
}
