package com.clm.api.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@lombok.RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final EmailVerificationService emailVerificationService;

  /**
   * Register a new account with the given role.
   *
   * @param request sign up request with the given role
   * @return the sign up response entity
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
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
   * @param request the request with the refresh token
   * @return the new access token and new refresh token
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.refreshToken(request));
  }

  @GetMapping("/two-step-verification/email")
  public ResponseEntity<?> verifyEmailVerificationLink(
      @RequestParam("token") String token, @RequestParam("email") String email) {
    return ResponseEntity.ok(emailVerificationService.verify(email, Map.of("token", token)));
  }

  @PostMapping("/two-step-verification/email")
  public ResponseEntity<?> resendEmailVerification(HttpServletRequest request) {
    return ResponseEntity.ok(authService.resendVerificationEmail(request));
  }
}
