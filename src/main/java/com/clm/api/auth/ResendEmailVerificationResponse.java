package com.clm.api.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

/** LoginResponse */
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResendEmailVerificationResponse {
  private String resendVerificationLinkToken;
}
