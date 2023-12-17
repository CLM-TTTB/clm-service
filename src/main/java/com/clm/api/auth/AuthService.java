package com.clm.api.auth;

import com.clm.api.exceptions.business.AlreadyExistsException;
import com.clm.api.exceptions.business.HttpHeaderMissingException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.exceptions.business.TokenRefreshException;
import jakarta.servlet.http.HttpServletRequest;

/** AuthService */
public interface AuthService {

  /**
   * Logs in the user with the provided credentials and returns an LoginResponse DTO containing
   * authentication and authorization tokens and some user information.
   *
   * @param loginRequest DTO containing user credentials for login.
   * @return LoginResponse DTO containing authentication and authorization tokens.
   */
  LoginResponse login(LoginRequest loginRequest);

  /**
   * Refreshes the user's access and refresh tokens using the information provided in the request
   * header.
   *
   * @param request Servlet request object containing the Authorization header.
   * @return RefreshTokenResponse DTO containing refreshed authentication and authorization tokens.
   * @throws TokenRefreshException if token refreshing fails due to invalid tokens or other issues.
   * @throws HttpHeaderMissingException if the Authorization header is missing or invalid.
   */
  RefreshTokenResponse refreshToken(HttpServletRequest request)
      throws TokenRefreshException, HttpHeaderMissingException;

  /**
   * Registers a new account with the given information and chosen roles,
   *
   * @param request DTO containing user information for registration.
   * @return boolean true if the user was successfully registered.
   * @throws AlreadyExistsException if the user already exists.
   * @throws NotFoundException if a required resource cannot be found.
   */
  ResendEmailVerificationResponse register(RegisterRequest request)
      throws AlreadyExistsException, NotFoundException;

  /**
   * Resends the email verification email to the user with the given email address.
   *
   * @param request DTO containing the email address of the user to resend the verification email
   *     to.
   * @return boolean true if the email was successfully sent.
   * @throws NotFoundException if the user cannot be found.
   * @throws AlreadyExistsException if the user has already been verified.
   */
  ResendEmailVerificationResponse resendVerificationEmail(HttpServletRequest request)
      throws NotFoundException, AlreadyExistsException;
}
