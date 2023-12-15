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

  public static final String USER_OR_PASSWORD_INCORRECT = "Username or password is incorrect";

  public static final String LOGIN_REQUIRED = "Please login to access this resource";

  public static final String NUMBER_OF_TEAMS_INVALID =
      "Number of teams is invalid, must be at least 2";

  public static final String NUMBER_OF_PLAYERS_INVALID =
      "Number of players is invalid, must be at least 2";

  public static final String PHONE_NUMBER_INVALID = "Phone number is invalid";

  public static final String AGE_INVALID = "Age is invalid, must be at least 6";

  public static final String AGE_GROUP_INVALID = "Age group is invalid";
  public static final String TEAM_MEMBER_LIST_EMPTY = "Team member list is empty";
  public static final String UNIFORM_LIST_EMPTY = "Uniform list is empty";
}
