package com.chalnakchalnak.account.account;

public record RegisterAccountCommand(
        String memberId,
        String password,
        String nickname,
        String phoneNumber
) {
}
