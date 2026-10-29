package com.chalnakchalnak.account.profile.adapter.in.web;

import com.chalnakchalnak.account.common.entity.ExceptionResponseEntity;
import com.chalnakchalnak.account.common.error.AccountErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(assignableTypes = ProfileImageController.class)
public class ProfileImageExceptionHandler {
  @ExceptionHandler({RuntimeException.class, MissingServletRequestParameterException.class})
  public ResponseEntity<ExceptionResponseEntity<Void>> handleImageError(Exception exception) {
    AccountErrorCode code = AccountErrorCode.INTERNAL_SERVER_ERROR;
    if (exception instanceof MissingServletRequestParameterException) {
      code = AccountErrorCode.BAD_REQUEST_INVALID_PARAM;
    } else if (exception instanceof ResponseStatusException statusException) {
      code =
          switch (statusException.getStatusCode().value()) {
            case 400 -> AccountErrorCode.BAD_REQUEST_INVALID_PARAM;
            case 404 -> AccountErrorCode.NOT_FOUND;
            case 413 -> AccountErrorCode.PAYLOAD_TOO_LARGE;
            default -> AccountErrorCode.INTERNAL_SERVER_ERROR;
          };
    }
    if (code == AccountErrorCode.INTERNAL_SERVER_ERROR) {
      log.warn("Profile image read failed ({})", exception.getClass().getSimpleName());
    }
    return ResponseEntity.status(code.getHttpStatusCode())
        .cacheControl(CacheControl.noStore())
        .body(new ExceptionResponseEntity<>(code));
  }
}
