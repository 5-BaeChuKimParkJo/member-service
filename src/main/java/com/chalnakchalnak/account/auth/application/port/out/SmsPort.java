package com.chalnakchalnak.account.auth.application.port.out;

public interface SmsPort {

  void sendSms(String phoneNumber, String code);
}
