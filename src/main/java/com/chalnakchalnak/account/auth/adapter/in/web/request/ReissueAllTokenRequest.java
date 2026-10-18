package com.chalnakchalnak.account.auth.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueAllTokenRequest {

  @NotBlank(message = "Refresh token은 필수 값입니다.")
  private String refreshToken;
}
