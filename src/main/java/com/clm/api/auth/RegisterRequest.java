package com.clm.api.auth;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

/** RegisterRequest */
@lombok.Getter
@lombok.Setter
public class RegisterRequest {

  @Email(message = ErrorMessage.EMAIL_INVALID)
  @NotBlank(message = ErrorMessage.EMAIL_REQUIRED)
  private String email;

  @NotBlank(message = ErrorMessage.EMAIL_REQUIRED)
  @Pattern(regexp = Regex.PASSWORD, message = ErrorMessage.PASSWORD_REQUIRED)
  private String password;

  private String name;

  private Set<Role> roles = new HashSet<>();

  public String getName() {
    if (name == null || name.trim().isEmpty()) {
      return "User" + System.currentTimeMillis();
    }
    return name;
  }
}
