package com.chalnakchalnak.member_service.auth.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IdentityVerificationPurpose {

    SIGN_UP("회원가입"),
    FIND_ID("아이디 찾기"),
    PASSWORD_RESET("비밀번호 재설정");

    private final String label;
}
