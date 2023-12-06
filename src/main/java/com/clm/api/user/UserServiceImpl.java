package com.clm.api.user;

import com.clm.api.exceptions.business.InvalidException;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** UserServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public void changePassword(ChangePasswordRequest request, Principal connectedUser)
      throws InvalidException {
    User user = PrincipalHelper.getUser(connectedUser);

    if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
      throw new InvalidException("New password and confirmation password are not the same");
    }

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new InvalidException(HttpStatus.FORBIDDEN, "Current password is not correct");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }

  @Override
  public String updateAvatar(String avatar, Principal connectedUser) {
    throw new UnsupportedOperationException("Unimplemented method 'updateAvatar'");
  }
}
