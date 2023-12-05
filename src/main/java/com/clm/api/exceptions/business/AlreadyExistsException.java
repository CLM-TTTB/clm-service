package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** AlreadyExistsException */
public class AlreadyExistsException extends BusinessException {
  public AlreadyExistsException() {
    super(HttpStatus.FORBIDDEN, "Item is already exists");
  }

  public AlreadyExistsException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }

  public AlreadyExistsException(HttpStatus status, String message) {
    super(status, message);
  }

  public AlreadyExistsException(String message, Throwable cause) {
    super(HttpStatus.FORBIDDEN, message, cause);
  }

  public AlreadyExistsException(HttpStatus status, String message, Throwable cause) {
    super(status, message, cause);
  }
}
