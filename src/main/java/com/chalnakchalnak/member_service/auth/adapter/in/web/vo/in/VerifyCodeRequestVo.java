package com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in;

import com.chalnakchalnak.member_service.auth.domain.model.enums.IdentityVerificationPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class VerifyCodeRequestVo {

    @NotBlank(message = "phoneNumber 는 필수 값입니다.")
    @Pattern(regexp = "^\\d{9,11}$", message = "전화번호는 숫자만 포함하며 9~11자리여야 합니다.")
    private String phoneNumber;
    @NotBlank(message = "VerificationCode 는 필수 값입니다.")
    @Pattern(regexp = "^\\d{6}$", message = "인증 코드는 숫자 6자리여야 합니다.")
    private String verificationCode;
    @NotNull(message = "purpose 는 필수 값입니다.")
    private IdentityVerificationPurpose purpose;
}
