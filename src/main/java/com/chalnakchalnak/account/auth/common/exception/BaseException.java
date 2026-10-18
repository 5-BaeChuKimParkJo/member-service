package com.chalnakchalnak.account.auth.common.exception;

import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  private final AuthErrorCode status;

  public BaseException(AuthErrorCode status) {
    this.status = status;
  }
}
