package com.chalnakchalnak.member_service.infrastructure.custom;

import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.member_service.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    long updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto);
}
