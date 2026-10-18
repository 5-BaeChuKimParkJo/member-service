package com.chalnakchalnak.account.auth.common.exception;

import com.chalnakchalnak.account.auth.common.response.AuthErrorCode;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Hidden
@RestControllerAdvice
@Slf4j
public class AuthBaseExceptionHandler {

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
        new ExceptionResponseEntity<>(AuthErrorCode.INTERNAL_SERVER_ERROR);
    log.error("RuntimeException: ", e);
    for (StackTraceElement s : e.getStackTrace()) {
      System.out.println(s);
    }
    return new ResponseEntity<>(response, response.httpStatus());
  }

  @ExceptionHandler(BadCredentialsException.class)
  protected ResponseEntity<ExceptionResponseEntity<Void>> handleBadCredentialsException(
      BadCredentialsException e) {
    ExceptionResponseEntity<Void> response =
        new ExceptionResponseEntity<>(AuthErrorCode.FAILED_TO_LOGIN);
    log.error("BadCredentialsException: ", e);
    return new ResponseEntity<>(response, response.httpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ExceptionResponseEntity<Void>> handleValidationException(
      MethodArgumentNotValidException e) {
    log.warn("ValidationException: {}", e.getMessage());

    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    ExceptionResponseEntity<Void> response =
        new ExceptionResponseEntity<>(AuthErrorCode.INVALID_INPUT, message);

    return new ResponseEntity<>(response, response.httpStatus());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<ExceptionResponseEntity<Void>> handleJsonParseException(
      HttpMessageNotReadableException e) {
    log.warn("Json parsing error: {}", e.getMessage());

    Throwable cause = e.getCause();
    while (cause != null) {
      if (cause instanceof BaseException baseEx) {
        ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(baseEx.getStatus());
        return new ResponseEntity<>(response, response.httpStatus());
      }

      cause = cause.getCause();
    }

    ExceptionResponseEntity<Void> response =
        new ExceptionResponseEntity<>(AuthErrorCode.INVALID_INPUT);
    return new ResponseEntity<>(response, response.httpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponseEntity<Void>> handleAllExceptions(Exception ex) {
    AuthErrorCode status;

    if (ex instanceof HttpRequestMethodNotSupportedException) {
      status = AuthErrorCode.METHOD_NOT_ALLOWED;
    } else if (ex instanceof NoHandlerFoundException) {
      status = AuthErrorCode.NOT_FOUND;
    } else if (ex instanceof MethodArgumentTypeMismatchException) {
      status = AuthErrorCode.BAD_REQUEST_INVALID_PARAM;
    } else {
      ex.printStackTrace();
      status = AuthErrorCode.INTERNAL_SERVER_ERROR;
    }

    return new ResponseEntity<>(new ExceptionResponseEntity<>(status), status.getHttpStatusCode());
  }
}
