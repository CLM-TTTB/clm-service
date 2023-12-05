package com.clm.api.exceptions.handlers;

import com.clm.api.exceptions.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import org.springframework.http.HttpStatus;

/** ExceptionHandler */
public class CustomExceptionHandler {

  // private methods
  public ExceptionResponse buildErrorMessage(
      HttpStatus status, String message, Exception e, HttpServletRequest request) {
    return buildErrorMessage(status, message, e, request, null);
  }

  public ExceptionResponse buildErrorMessage(
      HttpStatus status, String message, Exception e, HttpServletRequest request, Object data) {
    return ExceptionResponse.builder()
        .status(status)
        .timestamp(new Date())
        .code(status.value())
        .message(message)
        .path(request != null ? request.getServletPath() : null)
        .data(data)
        .build();
  }
}
