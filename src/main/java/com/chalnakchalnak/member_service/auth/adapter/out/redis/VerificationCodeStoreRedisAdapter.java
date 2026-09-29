package com.chalnakchalnak.member_service.auth.adapter.out.redis;

import com.chalnakchalnak.member_service.auth.application.port.out.VerificationCodeStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class VerificationCodeStoreRedisAdapter implements VerificationCodeStorePort {
    private final StringRedisTemplate redisTemplate;

    private static final Duration SEND_LIMIT_TTL = Duration.ofMinutes(3);
    private static final Duration ATTEMPT_TTL = Duration.ofMinutes(5);
    private static final Duration GRANT_TTL = Duration.ofMinutes(10);

    // Key-Value 저장
    @Override
    public void saveCode(String phoneNumber, String code) {
        redisTemplate.opsForValue().set(codeKey(phoneNumber), code, ATTEMPT_TTL);
        redisTemplate.opsForValue().set(sendLimitKey(phoneNumber), "1", SEND_LIMIT_TTL); // 전송 제한 키
        redisTemplate.opsForValue().set(attemptKey(phoneNumber), "0", ATTEMPT_TTL);
    }

    // SMS 전송
    @Override
    public Boolean sendLimited(String phoneNumber) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(sendLimitKey(phoneNumber)));
    }

    @Override
    public int increaseVerifyAttempt(String phoneNumber) {
        return redisTemplate.opsForValue().increment(attemptKey(phoneNumber)).intValue();
    }

    @Override
    public void setGrantAccess(String phoneNumber, String purpose) {
        redisTemplate.opsForValue().set(grantKey(phoneNumber, purpose), "true", GRANT_TTL);
    }

    @Override
    public Boolean grantedAccess(String phoneNumber, String purpose) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(grantKey(phoneNumber, purpose)));
    }

    @Override
    public String findCode(String phoneNumber) {
        return redisTemplate.opsForValue().get(codeKey(phoneNumber));
    }

    @Override
    public void deleteCode(String phoneNumber) {
        redisTemplate.delete(codeKey(phoneNumber));
    }

    @Override
    public void deleteAttemptVerification(String phoneNumber) { redisTemplate.delete(attemptKey(phoneNumber)); }


    private String codeKey(String phoneNumber) { return "sms:" + phoneNumber; }
    private String sendLimitKey(String phoneNumber) { return "sms:limit:" + phoneNumber; }
    private String attemptKey(String phoneNumber) { return "sms:attempt:" + phoneNumber; }
    private String grantKey(String phoneNumber, String purpose) {
        return "sms:" + purpose + ":" + phoneNumber;
    }
}
