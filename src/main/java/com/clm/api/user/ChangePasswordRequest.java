package com.clm.api.user;

/** ChangePasswordRequest */
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ChangePasswordRequest {

  private String currentPassword;
  private String newPassword;
  private String confirmationPassword;
}
