package com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequestVo {

    @NotBlank(message = "memberId 는 필수 값입니다.")
    @Pattern(
            regexp = "^[a-z][a-z0-9]{3,19}$",
            message = "아이디는 소문자로 시작하고 영문자와 숫자 조합의 4~20자여야 합니다."
    )
    private String memberId;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8~20자여야 합니다."
    )
    private String password;

    @NotBlank
    @Size(min=2, max=10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;

    @NotBlank
    @Pattern(
            regexp = "^010\\d{4}\\d{4}$",
            message = "전화번호는 하이픈(-)없이 숫자만 입력해주세요."
    )
    private String phoneNumber;
}
