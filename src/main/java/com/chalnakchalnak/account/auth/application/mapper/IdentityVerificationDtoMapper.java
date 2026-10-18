package com.chalnakchalnak.account.auth.application.mapper;

import com.chalnakchalnak.account.auth.application.port.dto.in.VerifyCodeRequestDto;
import com.chalnakchalnak.account.auth.domain.model.IdentityVerificationDomain;
import org.springframework.stereotype.Component;

@Component
public class IdentityVerificationDtoMapper {

    public IdentityVerificationDomain toIdentityVerificationDomain(VerifyCodeRequestDto verifyCodeRequestDto) {
        return IdentityVerificationDomain.builder()
                .phoneNumber(verifyCodeRequestDto.getPhoneNumber())
                .verificationCode(verifyCodeRequestDto.getVerificationCode())
                .purpose(verifyCodeRequestDto.getPurpose())
                .build();
    }
}
