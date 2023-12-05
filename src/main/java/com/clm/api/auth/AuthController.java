package com.clm.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@lombok.RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * Register a new account with the given role.
   *
   * @param request sign up request with the given role
   * @return the sign up response entity
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
    return authService.register(request)
        ? ResponseEntity.status(HttpStatus.CREATED).build()
        : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  /**
   * Login to the application.
   *
   * @param loginRequest the login request
   * @return the jwt token and refresh token
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
    return ResponseEntity.ok(authService.login(loginRequest));
  }

  /**
   * Refresh the jwt token.
   *
   * @param request the request
   * @return the new access token and new refresh token
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.refreshToken(request));
  }
}
