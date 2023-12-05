package com.clm.api.constants.message;

/** ErrorMessage */
public class ErrorMessage {
  public static final String EMAIL_REQUIRED = "Email is required";
  public static final String EMAIL_INVALID = "Email is invalid";

  public static final String PASSWORD_REQUIRED = "Password is required";
  public static final String PASSWORD_INVALID =
      "Password must be at least 8 characters with at least one uppercase letter";

  public static final String REFRESH_TOKEN_INVALID = "Refresh token is invalid, please login again";
  public static final String REFRESH_TOKEN_REVOKED = "Refresh token is revoked, please login again";
  public static final String REFRESH_TOKEN_EXPIRED = "Refresh token is expired, please login again";

  public static final String USER_OR_PASSWORD_INVALID = "Username or password is invalid";
}
