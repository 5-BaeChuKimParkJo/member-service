package com.chalnakchalnak.member_service.auth.application.port.out;

public interface SmsPort {

    void sendSms(String phoneNumber, String code);
}
