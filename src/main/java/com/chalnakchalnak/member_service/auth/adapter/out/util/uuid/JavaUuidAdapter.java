package com.chalnakchalnak.member_service.auth.adapter.out.util.uuid;

import com.chalnakchalnak.member_service.auth.application.port.out.GenerateUuidPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JavaUuidAdapter implements GenerateUuidPort {

    @Override
    public String generateUuid() {
        return UUID.randomUUID().toString();
    }
}