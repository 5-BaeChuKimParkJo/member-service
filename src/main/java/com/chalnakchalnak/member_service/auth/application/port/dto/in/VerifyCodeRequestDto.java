package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import com.chalnakchalnak.member_service.auth.domain.model.enums.IdentityVerificationPurpose;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VerifyCodeRequestDto {

    private String phoneNumber;
    private String verificationCode;
    private IdentityVerificationPurpose purpose;

    @Builder
    public VerifyCodeRequestDto(
            String phoneNumber,
            String verificationCode,
            IdentityVerificationPurpose purpose
    ) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.purpose = purpose;
    }
}
