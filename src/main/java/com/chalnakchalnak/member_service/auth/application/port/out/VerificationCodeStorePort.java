package com.chalnakchalnak.member_service.auth.application.port.out;

public interface VerificationCodeStorePort {

    void saveCode(String phoneNumber, String code);
    Boolean sendLimited(String phoneNumber);
    int increaseVerifyAttempt(String phoneNumber);
    void setGrantAccess(String phoneNumber, String purpose);
    Boolean grantedAccess(String phoneNumber, String purpose);
    String findCode(String phoneNumber);
    void deleteCode(String phoneNumber);
    void deleteAttemptVerification(String phoneNumber);
}
