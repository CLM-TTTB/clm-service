package com.clm.api.exceptions.handlers;

import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.exceptions.ExceptionResponse;
import com.clm.api.exceptions.business.BusinessException;
import com.clm.api.exceptions.business.HttpHeaderMissingException;
import com.clm.api.exceptions.business.TokenRefreshException;
import com.clm.api.exceptions.business.UserUnverifiedException;
import com.twilio.exception.ApiException;
import com.twilio.exception.TwilioException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** AuthExceptionHandler */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionHandler extends CustomExceptionHandler {
  // business exceptions
  @ExceptionHandler({
    TokenRefreshException.class,
    HttpHeaderMissingException.class,
  })
  public ResponseEntity<?> handleExceptionA(BusinessException e, HttpServletRequest request) {
    return ResponseEntity.status(e.getStatus())
        .body(buildErrorMessage(e.getStatus(), e.getMessage(), e, request));
  }

  @ExceptionHandler({
    UserUnverifiedException.class,
  })
  public ResponseEntity<?> handleNotVerifiedAccountException(
      UserUnverifiedException e, HttpServletRequest request) {
    return ResponseEntity.status(e.getStatus())
        .body(buildErrorMessage(e.getStatus(), e.getMessage(), e, request, e.getUserStatus()));
  }

  // spring security exceptions
  @ExceptionHandler({
    BadCredentialsException.class,
    UsernameNotFoundException.class,
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ExceptionResponse handleBadCredentialsException(
      AuthenticationException e, HttpServletRequest request) {
    return buildErrorMessage(
        HttpStatus.NOT_FOUND, ErrorMessage.USER_OR_PASSWORD_INCORRECT, e, request);
  }

  @ExceptionHandler({
    AuthenticationException.class,
    ExpiredJwtException.class,
    AccessDeniedException.class
  })
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ExceptionResponse handleAuthenticationException(
      AuthenticationException e, HttpServletRequest request) {
    return buildErrorMessage(HttpStatus.UNAUTHORIZED, e.getMessage(), e, request);
  }

  // twilio exceptions
  @ExceptionHandler({ApiException.class})
  public ResponseEntity<?> handleApiException(ApiException e, HttpServletRequest request) {
    return ResponseEntity.status(e.getStatusCode())
        .body(buildErrorMessage(HttpStatus.valueOf(e.getStatusCode()), e.getMessage(), e, request));
  }

  @ExceptionHandler({TwilioException.class})
  public ResponseEntity<?> handleTwilioException(TwilioException e, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e, request));
  }
}
