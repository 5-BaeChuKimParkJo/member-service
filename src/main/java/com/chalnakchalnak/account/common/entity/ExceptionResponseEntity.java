package com.chalnakchalnak.account.common.entity;

import com.chalnakchalnak.account.common.error.AccountErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatusCode;

public record ExceptionResponseEntity<T>(
    @JsonIgnore HttpStatusCode httpStatus, String message, int code) {

  public ExceptionResponseEntity(AccountErrorCode status) {
    this(status.getHttpStatusCode(), status.getMessage(), status.getCode());
  }

  public ExceptionResponseEntity(AccountErrorCode status, String message) {
    this(status.getHttpStatusCode(), message, status.getCode());
  }
}
