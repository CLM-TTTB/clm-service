package com.clm.api.user;

import com.clm.api.exceptions.business.InvalidException;
import java.security.Principal;
import java.util.Map;

/** UserService */
public interface UserService {

  /**
   * Change the password of the connected user
   *
   * @param request The request containing the old and new password
   * @param connectedUser The connected user
   * @throws InvalidException If the old password is not correct
   */
  void changePassword(ChangePasswordRequest request, Principal connectedUser)
      throws InvalidException;

  /**
   * Update the avatar of the connected user
   *
   * @param avatar The new avatar
   * @param connectedUser The connected user
   * @return The new avatar
   */
  String updateAvatar(String avatar, Principal connectedUser);

  Map<String, Object> changeProfile(ChangeProfileRequest request, Principal connectedUser);
}
