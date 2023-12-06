package com.clm.api.utils;

import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/** PrincipalHelper */
public class PrincipalHelper {

  public static final User getUser(Principal connectedUser) {
    if (connectedUser == null) {
      throw new NotFoundException(HttpStatus.UNAUTHORIZED, ErrorMessage.LOGIN_REQUIRED);
    }

    final UsernamePasswordAuthenticationToken token =
        (UsernamePasswordAuthenticationToken) connectedUser;
    return (User) token.getPrincipal();
  }
}
