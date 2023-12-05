package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** BusinessException */
public abstract class BusinessException extends RuntimeException {
  protected HttpStatus status;

  public BusinessException() {
    super();
  }

  public BusinessException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

  public BusinessException(HttpStatus status, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
  }

  public int getCode() {
    return status.value();
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getMessage() {
    return super.getMessage();
  }
}
