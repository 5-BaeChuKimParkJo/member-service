package com.chalnakchalnak.account.auth.application.port.in;

import com.chalnakchalnak.account.auth.application.command.SendVerificationCodeCommand;
import com.chalnakchalnak.account.auth.application.command.VerifyCodeCommand;

public interface IdentityVerificationUseCase {

  void sendVerificationCode(SendVerificationCodeCommand sendVerificationCodeRequestDto);

  Boolean verifyCode(VerifyCodeCommand verifyCodeRequestDto);
}
