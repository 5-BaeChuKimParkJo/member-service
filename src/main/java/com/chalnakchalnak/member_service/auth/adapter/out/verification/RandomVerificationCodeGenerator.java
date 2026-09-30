package com.chalnakchalnak.member_service.auth.adapter.out.verification;

import com.chalnakchalnak.member_service.auth.application.port.out.VerificationCodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@ConditionalOnProperty(prefix = "verification", name = "mode", havingValue = "sms", matchIfMissing = true)
public class RandomVerificationCodeGenerator implements VerificationCodeGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generate() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }
}
