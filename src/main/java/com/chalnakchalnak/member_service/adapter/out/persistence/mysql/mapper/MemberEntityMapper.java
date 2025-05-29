package com.chalnakchalnak.member_service.adapter.out.persistence.mysql.mapper;

import com.chalnakchalnak.member_service.adapter.out.persistence.mysql.entity.MemberEntity;
import com.chalnakchalnak.member_service.application.port.in.dto.SignUpRequestDto;
import org.springframework.stereotype.Component;

@Component
public class MemberEntityMapper {

    public MemberEntity toEntity(SignUpRequestDto signUpRequestDto) {
        return MemberEntity.builder()
                .memberUuid(signUpRequestDto.getMemberUuid())
                .nickname(signUpRequestDto.getNickname())
                .build();
    }
}
