package com.chalnakchalnak.account.auth.adapter.in.web.vo.out;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMemberIdResponseVo {

    private String memberId;

    @Builder
    public GetMemberIdResponseVo(String memberId) {
        this.memberId = memberId;
    }
}
