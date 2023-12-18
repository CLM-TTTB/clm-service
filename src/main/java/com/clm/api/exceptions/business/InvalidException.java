package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** InvalidException */
public class InvalidException extends BusinessException {

  public InvalidException() {
    super(HttpStatus.FORBIDDEN, "Invalid");
  }

  public InvalidException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }

  public InvalidException(HttpStatus status, String message) {
    super(status, message);
  }

  public InvalidException(String message, Throwable cause) {
    super(HttpStatus.FORBIDDEN, message, cause);
  }

  public InvalidException(HttpStatus status, String message, Throwable cause) {
    super(status, message, cause);
  }
}
