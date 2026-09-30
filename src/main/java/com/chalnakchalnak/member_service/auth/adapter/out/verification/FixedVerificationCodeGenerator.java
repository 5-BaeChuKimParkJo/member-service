package com.chalnakchalnak.member_service.auth.adapter.out.verification;

import com.chalnakchalnak.member_service.auth.application.port.out.VerificationCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "verification", name = "mode", havingValue = "fixed")
public class FixedVerificationCodeGenerator implements VerificationCodeGenerator {

    private final String code;

    public FixedVerificationCodeGenerator(@Value("${verification.fixed-code}") String code) {
        this.code = code;
    }

    @Override
    public String generate() {
        return code;
    }
}
