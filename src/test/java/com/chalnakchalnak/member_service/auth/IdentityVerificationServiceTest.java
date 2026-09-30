package com.chalnakchalnak.member_service.auth;

import com.chalnakchalnak.member_service.auth.application.mapper.IdentityVerificationDtoMapper;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.SendVerificationCodeRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.dto.in.VerifyCodeRequestDto;
import com.chalnakchalnak.member_service.auth.application.port.out.SmsPort;
import com.chalnakchalnak.member_service.auth.application.port.out.VerificationCodeGenerator;
import com.chalnakchalnak.member_service.auth.application.port.out.VerificationCodeStorePort;
import com.chalnakchalnak.member_service.auth.application.service.IdentityVerificationService;
import com.chalnakchalnak.member_service.auth.common.exception.BaseException;
import com.chalnakchalnak.member_service.auth.common.response.BaseResponseStatus;
import com.chalnakchalnak.member_service.auth.domain.model.enums.IdentityVerificationPurpose;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IdentityVerificationServiceTest {

    private static final String PHONE = "01012345678";

    private SmsPort smsPort;
    private VerificationCodeStorePort store;
    private VerificationCodeGenerator generator;
    private IdentityVerificationService service;

    @BeforeEach
    void setUp() {
        smsPort = mock(SmsPort.class);
        store = mock(VerificationCodeStorePort.class);
        generator = mock(VerificationCodeGenerator.class);
        service = new IdentityVerificationService(
                smsPort,
                store,
                new IdentityVerificationDtoMapper(),
                generator
        );
    }

    @Test
    void storesAndSendsTheConfiguredCode() {
        when(store.sendLimited(PHONE)).thenReturn(false);
        when(generator.generate()).thenReturn("000000");

        service.sendVerificationCode(sendRequest());

        verify(smsPort).sendSms(PHONE, "000000");
        verify(store).saveCode(PHONE, "000000");
    }

    @Test
    void wrongCodeDoesNotGrantSignupAccess() {
        when(store.findCode(PHONE)).thenReturn("000000");
        when(store.increaseVerifyAttempt(PHONE)).thenReturn(1);

        assertThat(service.verifyCode(verifyRequest("111111"))).isFalse();

        verify(store, never()).setGrantAccess(PHONE, IdentityVerificationPurpose.SIGN_UP.toString());
    }

    @Test
    void missingStoredCodeIsExpired() {
        when(store.findCode(PHONE)).thenReturn(null);

        assertThatThrownBy(() -> service.verifyCode(verifyRequest("000000")))
                .isInstanceOf(BaseException.class)
                .extracting("status")
                .isEqualTo(BaseResponseStatus.EXPIRED_VERIFICATION_CODE);
    }

    @Test
    void fifthWrongAttemptInvalidatesTheCode() {
        when(store.findCode(PHONE)).thenReturn("000000");
        when(store.increaseVerifyAttempt(PHONE)).thenReturn(5);

        assertThatThrownBy(() -> service.verifyCode(verifyRequest("111111")))
                .isInstanceOf(BaseException.class)
                .extracting("status")
                .isEqualTo(BaseResponseStatus.VERIFICATION_LIMITED);

        verify(store).deleteCode(PHONE);
        verify(store).deleteAttemptVerification(PHONE);
        verify(store, never()).setGrantAccess(PHONE, IdentityVerificationPurpose.SIGN_UP.toString());
    }

    @Test
    void correctCodeCreatesTheExistingSignupGrant() {
        when(store.findCode(PHONE)).thenReturn("000000");

        assertThat(service.verifyCode(verifyRequest("000000"))).isTrue();

        verify(store).setGrantAccess(PHONE, IdentityVerificationPurpose.SIGN_UP.toString());
        verify(store).deleteCode(PHONE);
        verify(store).deleteAttemptVerification(PHONE);
    }

    private SendVerificationCodeRequestDto sendRequest() {
        return SendVerificationCodeRequestDto.builder()
                .phoneNumber(PHONE)
                .purpose(IdentityVerificationPurpose.SIGN_UP)
                .build();
    }

    private VerifyCodeRequestDto verifyRequest(String code) {
        return VerifyCodeRequestDto.builder()
                .phoneNumber(PHONE)
                .verificationCode(code)
                .purpose(IdentityVerificationPurpose.SIGN_UP)
                .build();
    }
}
