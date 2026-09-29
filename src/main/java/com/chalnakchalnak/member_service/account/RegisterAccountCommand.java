package com.chalnakchalnak.member_service.account;

public record RegisterAccountCommand(
        String memberId,
        String password,
        String nickname,
        String phoneNumber
) {
}
