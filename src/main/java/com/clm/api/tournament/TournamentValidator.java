package com.clm.api.tournament;

import com.clm.api.enums.Visibility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/** MatchValidator */
public class TournamentValidator implements ConstraintValidator<ValidTournament, Tournament> {

  @Override
  public boolean isValid(Tournament value, ConstraintValidatorContext context) {

    if (value == null) {
      addConstraintViolation(context, "Tournament cannot be null");
      return false;
    }

    if (value.getVisibility() == Visibility.PRIVATE && !value.isViewOnly()) {
      addConstraintViolation(context, "Private matches must be view only");
      return false;
    }

    if (value.getMinTeams() > value.getMaxTeams()) {
      addConstraintViolation(context, "Min teams cannot be greater than max teams");
      return false;
    }

    if (value.getMinPlayersPerTeam() > value.getMaxPlayersPerTeam()) {
      addConstraintViolation(
          context, "Min players per team cannot be greater than max players per team");
      return false;
    }

    if (value.getStartTime().isAfter(value.getEndTime())) {
      addConstraintViolation(context, "Start time cannot be after end time");
      return false;
    }

    if (value.getStartTime().isBefore(value.getRegistrationDeadline())) {
      addConstraintViolation(context, "Start time cannot be before registration deadline");
      return false;
    }

    return true;
  }

  private void addConstraintViolation(ConstraintValidatorContext context, String message) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
  }
}
