package com.chalnakchalnak.member_service.auth.application.port.out.feign.member;

import com.chalnakchalnak.member_service.auth.application.port.dto.feign.member.CreateMemberRequestDto;

public interface MemberServicePort {

    void createMember(CreateMemberRequestDto createMemberRequestDto);

    Boolean existsByNickname(String nickname);
}
