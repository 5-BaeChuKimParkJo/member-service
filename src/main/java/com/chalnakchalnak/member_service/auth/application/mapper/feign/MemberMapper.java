package com.chalnakchalnak.member_service.auth.application.mapper.feign;

import com.chalnakchalnak.member_service.auth.application.port.dto.feign.member.CreateMemberRequestDto;
import com.chalnakchalnak.member_service.auth.domain.model.AuthDomain;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public CreateMemberRequestDto toCreateMemberRequestDto(AuthDomain authDomain) {
        return CreateMemberRequestDto.builder()
                .memberUuid(authDomain.getMemberUuid())
                .nickname(authDomain.getNickname())
                .build();
    }
}
