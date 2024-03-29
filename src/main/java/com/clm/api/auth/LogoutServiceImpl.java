package com.clm.api.auth;

import com.clm.api.exceptions.business.HttpHeaderMissingException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.security.RefreshToken;
import com.clm.api.security.RefreshTokenRepository;
import com.clm.api.utils.HttpHeaderHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerExceptionResolver;

/** LogoutService */
@Service
public class LogoutServiceImpl implements LogoutHandler {

  private RefreshTokenRepository refreshTokenRepository;
  private HandlerExceptionResolver resolver;

  public LogoutServiceImpl(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
      RefreshTokenRepository refreshTokenRepository) {
    this.resolver = resolver;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  @Transactional
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    try {
      final String refreshToken = HttpHeaderHelper.getBearerToken(request);

      RefreshToken savedRefreshToken =
          refreshTokenRepository
              .findByToken(refreshToken)
              .orElseThrow(() -> new NotFoundException("Refresh token not found"));

      refreshTokenRepository.save(savedRefreshToken.revoke());
      SecurityContextHolder.clearContext();
      response.setStatus(HttpStatus.NO_CONTENT.value());

    } catch (NotFoundException notFoundException) {
      resolver.resolveException(request, response, null, notFoundException);
    } catch (HttpHeaderMissingException httpHeaderMissingException) {
      resolver.resolveException(request, response, null, httpHeaderMissingException);
    } catch (Exception e) {
      resolver.resolveException(request, response, null, e);
    }
  }
}
