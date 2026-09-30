package com.chalnakchalnak.member_service.auth.application.port.out;

import com.chalnakchalnak.member_service.auth.application.port.dto.SignInDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.out.SignInResponseDto;

public interface AuthSecurityPort {

    String encryptPassword(String password);
    SignInResponseDto signIn(SignInDto signInDto, String inputPassword);
    String getMemberUuidByRefreshToken(String token);
    SignInResponseDto generateAllToken(String memberUuid);
}
