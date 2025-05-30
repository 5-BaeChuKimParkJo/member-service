package com.chalnakchalnak.member_service.common.exception;

import com.chalnakchalnak.member_service.common.entity.BaseResponseStatus;
import com.chalnakchalnak.member_service.common.entity.ExceptionResponseEntity;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
@Slf4j
public class BaseExceptionHandler {

    /**
     * 발생한 예외 처리
     */

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ExceptionResponseEntity<Void>> BaseError(BaseException e) {
        ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(e.getStatus());
        log.error("BaseException -> {}({})", e.getStatus(), e.getStatus().getMessage(), e);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ExceptionResponseEntity<Void>> RuntimeError(RuntimeException e) {
        ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        log.error("RuntimeException: ", e);
        for (StackTraceElement s : e.getStackTrace()) {
            System.out.println(s);
        }
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponseEntity<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("ValidationException: {}", e.getMessage());

        String errorCode = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        BaseResponseStatus status;

        try {
            status = BaseResponseStatus.valueOf(errorCode);
        } catch (IllegalArgumentException ex) {
            status = BaseResponseStatus.INVALID_INPUT; // 기본 fallback
        }

        ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(status);
        return new ResponseEntity<>(response, response.httpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ExceptionResponseEntity<Void>> handleJsonParseException(HttpMessageNotReadableException e) {
        log.warn("Json parsing error: {}", e.getMessage());

        // BaseException이 중첩돼 있는지 순회하면서 찾음
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof BaseException baseEx) {
                ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(baseEx.getStatus());
                return new ResponseEntity<>(response, response.httpStatus());
            }

            cause = cause.getCause();  // 깊이 파고들기
        }

        // BaseException이 아닌 경우는 일반 INVALID_INPUT으로 처리
        ExceptionResponseEntity<Void> response = new ExceptionResponseEntity<>(BaseResponseStatus.INVALID_INPUT);
        return new ResponseEntity<>(response, response.httpStatus());
    }
}