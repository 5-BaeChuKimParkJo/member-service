package com.chalnakchalnak.account.auth.application.service;

import com.chalnakchalnak.account.auth.application.command.SendVerificationCodeCommand;
import com.chalnakchalnak.account.auth.application.command.VerifyCodeCommand;
import com.chalnakchalnak.account.auth.application.mapper.IdentityVerificationMapper;
import com.chalnakchalnak.account.auth.application.port.in.IdentityVerificationUseCase;
import com.chalnakchalnak.account.auth.application.port.out.SmsPort;
import com.chalnakchalnak.account.auth.application.port.out.VerificationCodeGenerator;
import com.chalnakchalnak.account.auth.application.port.out.VerificationCodeStorePort;
import com.chalnakchalnak.account.auth.common.exception.BaseException;
import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import com.chalnakchalnak.account.auth.domain.model.IdentityVerification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdentityVerificationService implements IdentityVerificationUseCase {

  private final SmsPort smsPort;
  private final VerificationCodeStorePort verificationCodeStorePort;
  private final IdentityVerificationMapper identityVerificationDtoMapper;
  private final VerificationCodeGenerator verificationCodeGenerator;

  @Override
  @Transactional
  public void sendVerificationCode(SendVerificationCodeCommand sendVerificationCodeRequestDto) {
    if (verificationCodeStorePort.sendLimited(sendVerificationCodeRequestDto.getPhoneNumber())) {
      throw new BaseException(AuthErrorCode.SEND_LIMITED);
    }

    final String verificationCode = verificationCodeGenerator.generate();

    smsPort.sendSms(sendVerificationCodeRequestDto.getPhoneNumber(), verificationCode);
    verificationCodeStorePort.saveCode(
        sendVerificationCodeRequestDto.getPhoneNumber(), verificationCode);
  }

  @Override
  @Transactional
  public Boolean verifyCode(VerifyCodeCommand verifyCodeRequestDto) {
    final IdentityVerification identityVerificationDomain =
        identityVerificationDtoMapper.toIdentityVerificationDomain(verifyCodeRequestDto);
    final String storedCode =
        verificationCodeStorePort.findCode(identityVerificationDomain.getPhoneNumber());

    if (storedCode == null) {
      throw new BaseException(AuthErrorCode.EXPIRED_VERIFICATION_CODE);
    }

    final Boolean codeValid = identityVerificationDomain.verifyCode(storedCode);

    if (codeValid) {
      verificationCodeStorePort.setGrantAccess(
          identityVerificationDomain.getPhoneNumber(),
          identityVerificationDomain.getPurpose().toString());
      deleteVerificationCode(identityVerificationDomain.getPhoneNumber());
    } else {
      if (verificationCodeStorePort.increaseVerifyAttempt(
              identityVerificationDomain.getPhoneNumber())
          >= 5) {
        deleteVerificationCode(identityVerificationDomain.getPhoneNumber());
        throw new BaseException(AuthErrorCode.VERIFICATION_LIMITED);
      }
    }

    return codeValid;
  }

  @Transactional
  public void deleteVerificationCode(String phoneNumber) {
    verificationCodeStorePort.deleteCode(phoneNumber);
    verificationCodeStorePort.deleteAttemptVerification(phoneNumber);
  }
}
