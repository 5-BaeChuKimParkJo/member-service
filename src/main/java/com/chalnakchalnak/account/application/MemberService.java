package com.chalnakchalnak.account.application;

import com.chalnakchalnak.account.dto.in.MemberUpdateRequestDto;
import com.chalnakchalnak.account.dto.in.MemberUuidListDto;
import com.chalnakchalnak.account.dto.in.SignUpRequestDto;
import com.chalnakchalnak.account.dto.out.ChatroomMemberResponseDto;
import com.chalnakchalnak.account.dto.out.MemberResponseDto;

import java.util.List;

public interface MemberService {

    ChatroomMemberResponseDto getChatMember(String memberUuid);

    MemberResponseDto getMember(String memberUuid);

    List<MemberResponseDto> getMemberList(MemberUuidListDto memberUuidListDto);

    List<MemberResponseDto> getAllMemberList();

    void updateDynamic(MemberUpdateRequestDto memberUpdateRequestDto);

    void signUp(SignUpRequestDto signUpRequestDto);

    Boolean existNickname(String nickname);
}
