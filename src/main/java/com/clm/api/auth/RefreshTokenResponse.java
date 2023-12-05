package com.clm.api.auth;

import com.clm.api.security.TokenType;

/** RefreshTokenResponse */
@lombok.Setter
@lombok.Getter
@lombok.AllArgsConstructor
public class RefreshTokenResponse {
  private String accessToken;
  private String refreshToken;
  private TokenType tokenType;

  public RefreshTokenResponse(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.tokenType = TokenType.BEARER;
  }
}
