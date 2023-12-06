package com.clm.api.auth;

import com.clm.api.security.TokenType;
import com.fasterxml.jackson.annotation.JsonInclude;

/** LoginResponse */
@lombok.Getter
@lombok.Setter
@lombok.Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
  private String name;
  private String avatar;

  private String accessToken;
  private String refreshToken;

  private TokenType tokenType;
}
