package com.clm.api.exceptions.business;

import com.clm.api.user.User;
import org.springframework.http.HttpStatus;

@lombok.Getter
public class UserUnverifiedException extends BusinessException {

  private User.Status userStatus;

  public UserUnverifiedException(User.Status userStatus) {
    this(HttpStatus.UNAUTHORIZED, "User is not verified, please verify your account", userStatus);
    this.userStatus = userStatus;
  }

  public UserUnverifiedException(String message, User.Status userStatus) {
    this(HttpStatus.UNAUTHORIZED, message, userStatus);
  }

  public UserUnverifiedException(HttpStatus status, String message, User.Status userStatus) {
    super(status, message);
    this.userStatus = userStatus;
  }

  public UserUnverifiedException(String message, User.Status userStatus, Throwable cause) {
    this(HttpStatus.UNAUTHORIZED, message, userStatus, cause);
  }

  public UserUnverifiedException(
      HttpStatus status, String message, User.Status userStatus, Throwable cause) {
    super(status, message, cause);
    this.userStatus = userStatus;
  }
}
