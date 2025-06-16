package com.chalnakchalnak.member_service.infrastructure.custom;

import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;

public interface MemberRepositoryCustom {

    long updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto);
}
