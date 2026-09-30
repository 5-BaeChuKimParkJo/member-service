package com.chalnakchalnak.member_service.auth.adapter.out.sms.fixed;

import com.chalnakchalnak.member_service.auth.application.port.out.SmsPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "verification", name = "mode", havingValue = "fixed")
public class FixedCodeSmsAdapter implements SmsPort {

    @Override
    public void sendSms(String phoneNumber, String code) {
        // Homelab mode intentionally performs no external SMS request.
    }
}
