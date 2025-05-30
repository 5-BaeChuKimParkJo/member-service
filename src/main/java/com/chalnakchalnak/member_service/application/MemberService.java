package com.chalnakchalnak.member_service.application;

import com.chalnakchalnak.member_service.dto.in.SignUpRequestDto;

public interface MemberService {

    void signUp(SignUpRequestDto signUpRequestDto);
    Boolean existNickname(String nickname);
}
