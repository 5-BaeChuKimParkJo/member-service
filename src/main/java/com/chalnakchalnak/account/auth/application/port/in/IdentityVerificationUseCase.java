package com.chalnakchalnak.account.auth.application.port.in;

import com.chalnakchalnak.account.auth.application.port.dto.in.SendVerificationCodeRequestDto;
import com.chalnakchalnak.account.auth.application.port.dto.in.VerifyCodeRequestDto;

public interface IdentityVerificationUseCase {

    void sendVerificationCode(SendVerificationCodeRequestDto sendVerificationCodeRequestDto);
    Boolean verifyCode(VerifyCodeRequestDto verifyCodeRequestDto);
}
