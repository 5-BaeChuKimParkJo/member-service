package com.chalnakchalnak.account.common.exception;

import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import com.chalnakchalnak.account.common.entity.ExceptionResponseEntity;
import com.chalnakchalnak.account.common.error.AccountErrorCode;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Hidden
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class BaseExceptionHandler {

  /** 발생한 예외 처리 */
  @ExceptionHandler(BaseException.class)
  protected ResponseEntity<ExceptionResponseEntity<Void>> BaseError(BaseException e) {
    ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(e.getStatus());
    log.error("BaseException -> {}({})", e.getStatus(), e.getStatus().getMessage(), e);
    return new ResponseEntity<>(response, response.httpStatus());
  }

  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<ExceptionResponseEntity<Void>> RuntimeError(RuntimeException e) {
    ExceptionResponseEntity<Void> response =
        new ExceptionResponseEntity<>(AccountErrorCode.INTERNAL_SERVER_ERROR);
    log.error("RuntimeException: ", e);
    return new ResponseEntity<>(response, response.httpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ExceptionResponseEntity<Void>> handleValidationException(
      MethodArgumentNotValidException e) {
    log.warn("ValidationException: {}", e.getMessage());

    String errorCode = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    try {
      var status = AccountErrorCode.valueOf(errorCode);
      return new ResponseEntity<>(
          new ExceptionResponseEntity<>(status), status.getHttpStatusCode());
    } catch (IllegalArgumentException ex) {
      var status = AccountErrorCode.INVALID_INPUT;
      return new ResponseEntity<>(
          new ExceptionResponseEntity<>(status, errorCode), status.getHttpStatusCode());
    }
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<?> handleMethodNotAllowed(
      HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
    // Method matching can fail before a controller is selected, so package-scoped advice cannot
    // apply.
    String path = request.getRequestURI().substring(request.getContextPath().length());
    for (String prefix :
        new String[] {
          "/api/v1/auth",
          "/auth-service/api/v1/auth",
          "/account-service/api/v1/auth",
          "/api/v1/identity-verification",
          "/auth-service/api/v1/identity-verification",
          "/account-service/api/v1/identity-verification"
        }) {
      if (path.equals(prefix) || path.startsWith(prefix + "/")) {
        return new ResponseEntity<>(
            new com.chalnakchalnak.account.auth.common.exception.ExceptionResponseEntity<>(
                AuthErrorCode.METHOD_NOT_ALLOWED),
            AuthErrorCode.METHOD_NOT_ALLOWED.getHttpStatusCode());
      }
    }
    return new ResponseEntity<>(
        new ExceptionResponseEntity<>(AccountErrorCode.METHOD_NOT_ALLOWED),
        AccountErrorCode.METHOD_NOT_ALLOWED.getHttpStatusCode());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<ExceptionResponseEntity<Void>> handleJsonParseException(
      HttpMessageNotReadableException e) {
    log.warn("Json parsing error: {}", e.getMessage());

    // BaseException이 중첩돼 있는지 순회하면서 찾음
    Throwable cause = e.getCause();
    while (cause != null) {
      if (cause instanceof BaseException baseEx) {
        ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(baseEx.getStatus());
        return new ResponseEntity<>(response, response.httpStatus());
      }

      cause = cause.getCause(); // 깊이 파고들기
    }

    // BaseException이 아닌 경우는 일반 INVALID_INPUT으로 처리
    ExceptionResponseEntity<Void> response =
        new ExceptionResponseEntity<>(AccountErrorCode.INVALID_INPUT);
    return new ResponseEntity<>(response, response.httpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponseEntity<Void>> handleAllExceptions(Exception ex) {
    AccountErrorCode status;

    if (ex instanceof NoHandlerFoundException) {
      status = AccountErrorCode.NOT_FOUND;
    } else if (ex instanceof MethodArgumentTypeMismatchException) {
      status = AccountErrorCode.BAD_REQUEST_INVALID_PARAM;
    } else {
      ex.printStackTrace(); // 서버 콘솔에 로그 출력
      status = AccountErrorCode.INTERNAL_SERVER_ERROR;
    }

    return new ResponseEntity<>(new ExceptionResponseEntity<>(status), status.getHttpStatusCode());
  }
}
