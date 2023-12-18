package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

/** TeamRegistrationFailedException */
@lombok.Getter
public class TeamRegistrationFailedException extends BusinessException {

  public static enum Reason {
    TOURNAMENT_NOT_FOUND,
    TOURNAMENT_IS_VIEW_ONLY,
    TOURNAMENT_CANCELLED,
    TOURNAMENT_ENROLLMENT_CLOSED,
    ALREADY_ENROLLED,
    MAX_TEAMS_REACHED,
    TEAM_TOO_SMALL,
    TEAM_TOO_BIG,
    TEAM_NAME_ALREADY_TAKEN,
  }

  private Reason reason;

  public TeamRegistrationFailedException(Reason reason) {
    this(reason, "Team registration failed for reason: " + reason.toString().toLowerCase());
  }

  public TeamRegistrationFailedException(Reason reason, String message) {
    this(HttpStatus.FORBIDDEN, reason, message);
  }

  public TeamRegistrationFailedException(HttpStatus status, Reason reason, String message) {
    super(status, message);
    this.reason = reason;
  }

  public TeamRegistrationFailedException(Reason reason, String message, Throwable cause) {
    this(HttpStatus.FORBIDDEN, reason, message, cause);
  }

  public TeamRegistrationFailedException(
      HttpStatus status, Reason reason, String message, Throwable cause) {
    super(status, message, cause);
    this.reason = reason;
  }
}
