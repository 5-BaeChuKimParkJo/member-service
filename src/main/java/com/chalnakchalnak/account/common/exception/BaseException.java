package com.chalnakchalnak.account.common.exception;

import com.chalnakchalnak.account.common.error.AccountErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

  private final AccountErrorCode status;

  public BaseException(AccountErrorCode status) {
    this.status = status;
  }
}
