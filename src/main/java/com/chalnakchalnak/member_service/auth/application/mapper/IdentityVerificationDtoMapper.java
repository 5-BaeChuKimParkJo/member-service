package com.chalnakchalnak.member_service.auth.application.mapper;

import com.chalnakchalnak.member_service.auth.application.port.dto.in.VerifyCodeRequestDto;
import com.chalnakchalnak.member_service.auth.domain.model.IdentityVerificationDomain;
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
