package com.clm.api.exceptions.handlers;

import com.clm.api.exceptions.business.AlreadyExistsException;
import com.clm.api.exceptions.business.BusinessException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.exceptions.business.OutOfLimitException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** ApplicationExceptionHandler */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DBQueryExceptionHandler extends CustomExceptionHandler {

  @ExceptionHandler({
    NotFoundException.class,
    AlreadyExistsException.class,
    OutOfLimitException.class
  })
  public ResponseEntity<?> handleApplicationException(
      BusinessException e, HttpServletRequest request) {
    return ResponseEntity.status(e.getStatus())
        .body(buildErrorMessage(e.getStatus(), e.getMessage(), e, request));
  }
}
