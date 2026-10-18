package com.chalnakchalnak.account.auth.adapter.out.util.uuid;

import com.chalnakchalnak.account.auth.application.port.out.GenerateUuidPort;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaUuidAdapter implements GenerateUuidPort {

  @Override
  public String generateUuid() {
    return UUID.randomUUID().toString();
  }
}
