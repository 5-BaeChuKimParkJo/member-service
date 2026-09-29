package com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExistsPhoneNumberRequestVo {

    @NotBlank
    @Pattern(
            regexp = "^010\\d{4}\\d{4}$",
            message = "전화번호는 하이픈(-)없이 숫자만 입력해주세요."
    )
    private String phoneNumber;
}
