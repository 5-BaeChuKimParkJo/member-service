package com.chalnakchalnak.member_service.auth.domain.model;

import com.chalnakchalnak.member_service.auth.domain.model.enums.IdentityVerificationPurpose;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IdentityVerificationDomain {

    private String phoneNumber;
    private String verificationCode;
    private IdentityVerificationPurpose purpose;

    @Builder
    public IdentityVerificationDomain(String phoneNumber, String verificationCode, IdentityVerificationPurpose purpose) {
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.purpose = purpose;
    }

    public Boolean verifyCode(String storedCode) {
        return this.verificationCode.equals(storedCode);
    }
}
