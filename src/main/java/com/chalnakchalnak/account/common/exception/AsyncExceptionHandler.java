package com.chalnakchalnak.account.common.exception;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    if (ex instanceof BaseException baseException) {
      log.error(
          "BaseException: [{}] {}",
          baseException.getStatus(),
          baseException.getStatus().getMessage());
    }
    log.error("EventException: ", ex);
  }
}
