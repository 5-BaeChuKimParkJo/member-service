package com.chalnakchalnak.member_service.application.port.in;

import com.chalnakchalnak.member_service.application.port.in.dto.SignUpRequestDto;

public interface MemberUseCase {

    void signUp(SignUpRequestDto signUpRequestDto);

    Boolean checkNickname(String nickname);
}
