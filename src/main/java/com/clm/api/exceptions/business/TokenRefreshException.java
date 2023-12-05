package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** TokenRefreshException */
public class TokenRefreshException extends BusinessException {

  public TokenRefreshException() {
    super(HttpStatus.FORBIDDEN, "Invalid refresh token, please login again");
  }

  public TokenRefreshException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }

  public TokenRefreshException(HttpStatus status, String message) {
    super(status, message);
  }

  public TokenRefreshException(String message, Throwable cause) {
    super(HttpStatus.FORBIDDEN, message, cause);
  }

  public TokenRefreshException(HttpStatus status, String message, Throwable cause) {
    super(status, message, cause);
  }
}
