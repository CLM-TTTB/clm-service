package com.clm.api.auth;

import com.clm.api.exceptions.ExceptionResponse;
import com.clm.api.exceptions.business.HttpHeaderMissingException;
import com.clm.api.security.RefreshToken;
import com.clm.api.security.RefreshTokenRepository;
import com.clm.api.utils.HttpHeaderHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/** LogoutService */
@Service
@lombok.RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {

  private final RefreshTokenRepository refreshTokenRepository;

  private void returnResponse(
      HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) {
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(status)
            .message(message)
            .path(request.getServletPath())
            .code(status.value())
            .timestamp(new Date())
            .build();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(status.value());
    try {
      new ObjectMapper().writeValue(response.getOutputStream(), exceptionResponse);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    try {
      final String refreshToken = HttpHeaderHelper.getBearerToken(request);

      RefreshToken savedRefreshToken =
          refreshTokenRepository.findByToken(refreshToken).orElse(null);

      if (savedRefreshToken == null) {
        returnResponse(request, response, HttpStatus.NOT_FOUND, "Refresh token not found");
        return;
      }

      refreshTokenRepository.save(savedRefreshToken.revoke());

      SecurityContextHolder.clearContext();
      response.setStatus(HttpStatus.OK.value());

    } catch (HttpHeaderMissingException e) {
      returnResponse(request, response, e.getStatus(), e.getMessage());
    } catch (Exception e) {
      returnResponse(request, response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
