package com.chalnakchalnak.account.auth.adapter.out.verification;

import com.chalnakchalnak.account.auth.application.port.out.VerificationCodeGenerator;
import java.security.SecureRandom;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
    prefix = "verification",
    name = "mode",
    havingValue = "sms",
    matchIfMissing = true)
public class RandomVerificationCodeGenerator implements VerificationCodeGenerator {

  private final SecureRandom secureRandom = new SecureRandom();

  @Override
  public String generate() {
    return String.format("%06d", secureRandom.nextInt(1_000_000));
  }
}
