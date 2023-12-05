package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** NotFoundException */
public class NotFoundException extends BusinessException {

  public NotFoundException() {
    super(HttpStatus.NOT_FOUND, "Data not found");
  }

  public NotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }

  public NotFoundException(HttpStatus status, String message) {
    super(HttpStatus.NOT_FOUND, message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(HttpStatus.NOT_FOUND, message, cause);
  }

  public NotFoundException(HttpStatus status, String message, Throwable cause) {
    super(status, message, cause);
  }
}
