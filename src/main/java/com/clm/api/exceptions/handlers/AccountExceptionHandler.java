package com.clm.api.exceptions.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** AccountExceptionHandler */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccountExceptionHandler extends CustomExceptionHandler {
  // @ExceptionHandler(ChangePasswordException.class)
  // public ResponseEntity<?> handleChangePaswordException(
  //     ChangePasswordException e, HttpServletRequest request) {
  //   return ResponseEntity.status(e.getStatus())
  //       .body(buildErrorMessage(e.getStatus(), e.getMessage(), e, request));
  // }
}
