package com.clm.api.exceptions.handlers;

import com.clm.api.exceptions.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/** ValidationExceptionHandler */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationExceptionHandler extends CustomExceptionHandler {

  @ExceptionHandler({
    DateTimeParseException.class,
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleDateTimeParseException(
      DateTimeParseException e, HttpServletRequest request) {
    return buildErrorMessage(
        HttpStatus.BAD_REQUEST,
        "Invalid date, or date format. Please use the format: "
            + DateTimeFormatter.ISO_DATE_TIME.toString(),
        e,
        request);
  }

  // validation exceptions
  @ExceptionHandler({
    MethodArgumentNotValidException.class,
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleHandlerMethodValidationException(
      MethodArgumentNotValidException e) {

    final Map<String, String> body = new HashMap<>();
    e.getAllErrors()
        .forEach(
            objectError -> {
              final String[] fieldError = objectError.getCodes()[0].split("\\.");
              body.put(fieldError[fieldError.length - 1], objectError.getDefaultMessage());
            });
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler({
    BindException.class,
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleBindException(BindException e) {

    final Map<String, String> body = new HashMap<>();
    e.getBindingResult()
        .getFieldErrors()
        .forEach(
            fieldError -> {
              body.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler({
    HandlerMethodValidationException.class,
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> handleHandlerMethodValidationException(
      HandlerMethodValidationException e) {

    final Map<String, String> body = new HashMap<>();
    e.getAllErrors()
        .forEach(
            objectError -> {
              final String[] fieldError = objectError.getCodes()[0].split("\\.");
              body.put(fieldError[fieldError.length - 1], objectError.getDefaultMessage());
            });
    return ResponseEntity.badRequest().body(body);
  }
}
