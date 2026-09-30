package com.chalnakchalnak.member_service.auth.application.service;

import com.chalnakchalnak.member_service.auth.application.mapper.IdentityVerificationDtoMapper;
import com.chalnakchalnak.member_service.auth.application.port.in.IdentityVerificationUseCase;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.SendVerificationCodeRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.VerifyCodeRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.out.SmsPort;
import com.chalnakchalnak.member_service.auth.application.port.out.VerificationCodeGenerator;
import com.chalnakchalnak.member_service.auth.application.port.out.VerificationCodeStorePort;
import com.chalnakchalnak.member_service.auth.common.exception.BaseException;
import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import com.chalnakchalnak.member_service.auth.domain.model.IdentityVerificationDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IdentityVerificationService implements IdentityVerificationUseCase {

    private final SmsPort smsPort;
    private final VerificationCodeStorePort verificationCodeStorePort;
    private final IdentityVerificationDtoMapper identityVerificationDtoMapper;
    private final VerificationCodeGenerator verificationCodeGenerator;

    @Override
    @Transactional
    public void sendVerificationCode(SendVerificationCodeRequestDto sendVerificationCodeRequestDto) {
        if (verificationCodeStorePort.sendLimited(sendVerificationCodeRequestDto.getPhoneNumber())) {
            throw new BaseException(BaseResponseStatus.SEND_LIMITED);
        }

        final String verificationCode = verificationCodeGenerator.generate();

        smsPort.sendSms(sendVerificationCodeRequestDto.getPhoneNumber(), verificationCode);
        verificationCodeStorePort.saveCode(sendVerificationCodeRequestDto.getPhoneNumber(), verificationCode);
    }

    @Override
    @Transactional
    public Boolean verifyCode(VerifyCodeRequestDto verifyCodeRequestDto) {
        final IdentityVerificationDomain identityVerificationDomain =
                identityVerificationDtoMapper.toIdentityVerificationDomain(verifyCodeRequestDto);
        final String storedCode = verificationCodeStorePort.findCode(identityVerificationDomain.getPhoneNumber());

        if (storedCode == null) {
            throw new BaseException(BaseResponseStatus.EXPIRED_VERIFICATION_CODE);
        }

        final Boolean codeValid = identityVerificationDomain.verifyCode(storedCode);

        if (codeValid) {
            verificationCodeStorePort.setGrantAccess(
                    identityVerificationDomain.getPhoneNumber(),
                    identityVerificationDomain.getPurpose().toString()
            );
            deleteVerificationCode(identityVerificationDomain.getPhoneNumber());
        } else {
            if (verificationCodeStorePort.increaseVerifyAttempt(identityVerificationDomain.getPhoneNumber()) >= 5) {
                deleteVerificationCode(identityVerificationDomain.getPhoneNumber());
                throw new BaseException(BaseResponseStatus.VERIFICATION_LIMITED);
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
