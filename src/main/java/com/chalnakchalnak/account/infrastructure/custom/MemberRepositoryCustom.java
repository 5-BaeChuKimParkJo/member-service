package com.chalnakchalnak.account.infrastructure.custom;

import com.chalnakchalnak.account.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.account.entity.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    long updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto);
}
