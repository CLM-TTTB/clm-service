package com.clm.api.security;

import com.clm.api.exceptions.business.HttpHeaderMissingException;
import com.clm.api.utils.HttpHeaderHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// This class helps us to validate the generated jwt token
@Component
@lombok.RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
  private static final long serialVersionUID = 1L;

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    if (request.getServletPath().contains("/v1/auth")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt;
    try {
      jwt = HttpHeaderHelper.getBearerToken(request);
    } catch (HttpHeaderMissingException e) {
      filterChain.doFilter(request, response);
      return;
    }

    final String username = jwtService.extractUsername(jwt);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // check if access token is valid and not expired and is not refresh token
      if (jwtService.isTokenValid(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    filterChain.doFilter(request, response);
  }
}
