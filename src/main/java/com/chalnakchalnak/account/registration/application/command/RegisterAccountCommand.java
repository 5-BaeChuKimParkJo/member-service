package com.chalnakchalnak.account.registration.application.command;

public record RegisterAccountCommand(
    String memberId, String password, String nickname, String phoneNumber) {}
