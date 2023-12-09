package com.clm.api.tournament;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TournamentValidator.class)
public @interface ValidTournament {
  String message() default "Match is invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
