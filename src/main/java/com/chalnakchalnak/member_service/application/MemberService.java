package com.chalnakchalnak.member_service.application;

import com.chalnakchalnak.member_service.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.member_service.dto.in.MemberUuidListDto;
import com.chalnakchalnak.member_service.dto.in.SignUpRequestDto;
import com.chalnakchalnak.member_service.dto.out.MemberResponseDto;

import java.util.List;

public interface MemberService {

    MemberResponseDto getMember(String memberUuid);

    List<MemberResponseDto> getMemberList(MemberUuidListDto memberUuidListDto);

    List<MemberResponseDto> getAllMemberList();

    void updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto);

    void deleteMember(String memberUuid);

    void signUp(SignUpRequestDto signUpRequestDto);

    Boolean existNickname(String nickname);
}
