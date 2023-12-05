package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** InvalidException */
public class InvalidException extends BusinessException {

  public InvalidException() {
    super(HttpStatus.BAD_REQUEST, "Invalid");
  }

  public InvalidException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }

  public InvalidException(HttpStatus status, String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }

  public InvalidException(String message, Throwable cause) {
    super(HttpStatus.BAD_REQUEST, message, cause);
  }

  public InvalidException(HttpStatus status, String message, Throwable cause) {
    super(status, message, cause);
  }
}
