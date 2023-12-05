package com.clm.api.exceptions.handlers;

import com.clm.api.exceptions.business.InvalidException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** ApplicationExceptionHandler */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler extends CustomExceptionHandler {

  @ExceptionHandler(InvalidException.class)
  public ResponseEntity<?> handleInvalidException(InvalidException e, HttpServletRequest request) {
    return ResponseEntity.status(e.getStatus())
        .body(buildErrorMessage(e.getStatus(), e.getMessage(), e, request));
  }
}
