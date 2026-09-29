package com.chalnakchalnak.member_service.auth.application.port.dto.in;

import com.chalnakchalnak.member_service.auth.domain.model.enums.IdentityVerificationPurpose;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SendVerificationCodeRequestDto {

    private String phoneNumber;
    private IdentityVerificationPurpose purpose;

    @Builder
    public SendVerificationCodeRequestDto(
            String phoneNumber,
            IdentityVerificationPurpose purpose
    ) {
        this.phoneNumber = phoneNumber;
        this.purpose = purpose;
    }
}
