package com.chalnakchalnak.member_service.vo.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberUuidListRequestVo {

    @NotBlank(message = "멤버uuid는 필수값 입니다.")
    List<String> memberUuidList;

    @Builder
    public MemberUuidListRequestVo(List<String> memberUuidList) {
        this.memberUuidList = memberUuidList;
    }
}
