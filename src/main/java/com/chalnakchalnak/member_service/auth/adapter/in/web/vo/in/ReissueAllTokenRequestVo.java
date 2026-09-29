package com.chalnakchalnak.member_service.auth.adapter.in.web.vo.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueAllTokenRequestVo {

    @NotBlank(message = "Refresh token은 필수 값입니다.")
    private String refreshToken;
}
