package com.chalnakchalnak.member_service.auth.application.port.in;

import com.chalnakchalnak.member_service.auth.application.port.dto.SignOutDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.*;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.GetMemberIdResponseDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.SignInResponseDto;

import java.util.Optional;

public interface AuthUseCase {

    void signUp(SignUpRequestDto signUpRequestDto);
    Boolean existsMemberId(ExistsMemberIdRequestDto existsMemberIdRequestDto);
    Boolean existsNickname(ExistsNicknameRequestDto existsNicknameRequestDto);
    Boolean existsPhoneNumber(ExistsPhoneNumberRequestDto existsPhoneNumberRequestDto);
    SignInResponseDto signIn(SignInRequestDto authSignInRequestDto);
    SignInResponseDto reissueAllToken(ReissueAllTokenRequestDto reissueAllTokenRequestDto);
    void signOut(SignOutDto signOutDto);
    GetMemberIdResponseDto getMemberId(GetMemberIdRequestDto getMemberIdRequestDto);
    void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto);
}
