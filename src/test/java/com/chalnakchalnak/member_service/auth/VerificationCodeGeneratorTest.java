package com.chalnakchalnak.member_service.auth;

import com.chalnakchalnak.member_service.auth.adapter.out.verification.FixedVerificationCodeGenerator;
import com.chalnakchalnak.member_service.auth.adapter.out.verification.RandomVerificationCodeGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VerificationCodeGeneratorTest {

    @Test
    void fixedModeReturnsTheConfiguredHomelabCode() {
        assertThat(new FixedVerificationCodeGenerator("000000").generate()).isEqualTo("000000");
    }

    @Test
    void smsModeGeneratesSixDigitsInsteadOfUsingTheFixedCode() {
        String generated = new RandomVerificationCodeGenerator().generate();

        assertThat(generated).matches("[0-9]{6}");
    }
}
