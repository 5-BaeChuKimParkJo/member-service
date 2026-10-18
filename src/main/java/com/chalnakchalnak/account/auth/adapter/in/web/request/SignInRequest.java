package com.chalnakchalnak.account.auth.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequest {

  @NotBlank(message = "아이디를 입력해주세요.")
  private String memberId;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;
}
