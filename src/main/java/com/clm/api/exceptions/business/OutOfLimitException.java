package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** OutOfLimitException */
public class OutOfLimitException extends BusinessException {

  public OutOfLimitException() {
    super(HttpStatus.FORBIDDEN, "Out of limit");
  }

  public OutOfLimitException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }

  public OutOfLimitException(HttpStatus status, String message) {
    super(status, message);
  }

  public OutOfLimitException(String message, Throwable cause) {
    super(HttpStatus.FORBIDDEN, message, cause);
  }

  public OutOfLimitException(HttpStatus status, String message, Throwable cause) {
    super(status, message, cause);
  }
}
