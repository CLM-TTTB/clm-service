package com.clm.api.exceptions.handlers;

import com.clm.api.exceptions.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** GlobalExceptionHandler */
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler extends CustomExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionResponse handleIllegalArgumentException(
      IllegalArgumentException e, HttpServletRequest request) {
    return buildErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage(), e, request);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionResponse handleUnwantedException(Exception e, HttpServletRequest request) {
    return buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e, request);
  }
}
