package com.chalnakchalnak.member_service.dto.in;

import com.chalnakchalnak.member_service.vo.in.MemberUuidListRequestVo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberUuidListDto {
    List<String> memberUuidList;

    @Builder
    public MemberUuidListDto(List<String> memberUuidList) {
        this.memberUuidList = memberUuidList;
    }

    public static MemberUuidListDto from(MemberUuidListRequestVo memberUuidListRequestVo) {
        return MemberUuidListDto.builder()
                .memberUuidList(memberUuidListRequestVo.getMemberUuidList())
                .build();
    }
}
