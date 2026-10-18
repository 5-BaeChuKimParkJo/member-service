package com.chalnakchalnak.account.auth.adapter.out.util.uuid;

import com.chalnakchalnak.account.auth.application.port.out.GenerateUuidPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JavaUuidAdapter implements GenerateUuidPort {

    @Override
    public String generateUuid() {
        return UUID.randomUUID().toString();
    }
}