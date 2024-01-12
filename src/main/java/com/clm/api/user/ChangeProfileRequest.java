package com.clm.api.user;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ChangeProfileRequest {

  private String name;
  private String phoneNo;
  private String avatar;
}
