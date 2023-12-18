package com.clm.api.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.validation.BindException;

/** Validator */
public class ValidationHelper {
  private static final Validator validator =
      Validation.buildDefaultValidatorFactory().getValidator();

  public static <T> T validate(@Valid T t) {
    Set<ConstraintViolation<T>> violations = validator.validate(t);
    if (!violations.isEmpty()) {
      BindException bindException = new BindException(t, t.getClass().getName());
      violations.forEach(
          violation ->
              bindException.rejectValue(
                  violation.getPropertyPath().toString(), "403", violation.getMessage()));
      throw new RuntimeException(bindException);
    }
    return t;
  }
}
