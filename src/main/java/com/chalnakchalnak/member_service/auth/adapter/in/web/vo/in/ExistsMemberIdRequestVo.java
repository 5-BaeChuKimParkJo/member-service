package com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExistsMemberIdRequestVo {

    @NotBlank(message = "memberId 는 필수 값입니다.")
    @Pattern(
            regexp = "^[a-z][a-z0-9]{3,19}$",
            message = "아이디는 소문자로 시작하고 영문자와 숫자 조합의 4~20자여야 합니다."
    )
    private String memberId;
}
