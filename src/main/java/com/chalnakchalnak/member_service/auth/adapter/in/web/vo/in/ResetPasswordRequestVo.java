package com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ResetPasswordRequestVo {

    @NotBlank(message = "phoneNumber 는 필수 값입니다.")
    @Pattern(
            regexp = "^010\\d{4}\\d{4}$",
            message = "전화번호는 하이픈(-)없이 숫자만 입력해주세요."
    )
    private String phoneNumber;

    @NotBlank(message = "newPassword 는 필수 값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8~20자여야 합니다."
    )
    private String newPassword;
}
