package com.clm.api.auth;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/** LoginRequest */
@lombok.Getter
@lombok.Setter
public class LoginRequest {
  @Email(message = ErrorMessage.EMAIL_INVALID)
  @NotBlank(message = ErrorMessage.EMAIL_REQUIRED)
  private String email;

  @NotBlank(message = ErrorMessage.EMAIL_REQUIRED)
  @Pattern(regexp = Regex.PASSWORD, message = ErrorMessage.PASSWORD_REQUIRED)
  private String password;
}
