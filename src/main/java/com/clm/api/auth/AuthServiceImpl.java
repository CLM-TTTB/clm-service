package com.clm.api.auth;

import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.exceptions.business.AlreadyExistsException;
import com.clm.api.exceptions.business.HttpHeaderMissingException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.exceptions.business.TokenRefreshException;
import com.clm.api.security.JwtService;
import com.clm.api.security.RefreshToken;
import com.clm.api.security.RefreshTokenRepository;
import com.clm.api.security.TokenType;
import com.clm.api.user.EmailVerificationToken;
import com.clm.api.user.Role;
import com.clm.api.user.RoleRepository;
import com.clm.api.user.RoleType;
import com.clm.api.user.User;
import com.clm.api.user.UserRepository;
import com.clm.api.utils.HttpHeaderHelper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@lombok.RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  private final RefreshTokenRepository refreshTokenRepository;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final EmailVerificationService emailVerificationService;

  @Value("${email.verification-expiration}")
  private long EMAIL_VERIFICATION_EXPIRATION;

  @Transactional
  public RefreshTokenResponse refreshToken(HttpServletRequest request)
      throws TokenRefreshException, HttpHeaderMissingException {
    final String refreshToken = HttpHeaderHelper.getBearerToken(request);

    RefreshToken savedRefreshToken =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(() -> new TokenRefreshException(ErrorMessage.REFRESH_TOKEN_INVALID));

    if (savedRefreshToken.isExpired()) {
      refreshTokenRepository.save(savedRefreshToken.revoke());
      throw new TokenRefreshException(ErrorMessage.REFRESH_TOKEN_EXPIRED);
    }

    if (savedRefreshToken.isRevoked()) {
      throw new TokenRefreshException(ErrorMessage.REFRESH_TOKEN_REVOKED);
    }

    String newAccessToken = jwtService.generateAccessToken(savedRefreshToken.getUsername());
    RefreshToken updatedRefreshToken = refreshTokenRepository.save(savedRefreshToken.updateToken());

    return new RefreshTokenResponse(newAccessToken, updatedRefreshToken.getToken());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    User user = (User) authentication.getPrincipal();

    String jwtToken = jwtService.generateAccessToken(user);
    RefreshToken refreshToken = refreshTokenRepository.save(jwtService.generateRefreshToken(user));

    return LoginResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken.getToken())
        .name(user.getName())
        .avatar(user.getAvatar())
        .tokenType(TokenType.BEARER)
        .build();
  }

  @Override
  public boolean register(RegisterRequest request) {
    return register(request, request.getRoles(), null);
  }

  @Transactional(rollbackFor = Exception.class)
  private boolean register(
      RegisterRequest request, Set<Role> roles, Function<User, Boolean> createUserInfo)
      throws AlreadyExistsException, NotFoundException {
    if (userRepository.existsByEmail(request.getEmail()))
      throw new AlreadyExistsException("Email already exists");

    EmailVerificationToken emailVerificationToken =
        new EmailVerificationToken(EMAIL_VERIFICATION_EXPIRATION);

    User newUser =
        User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .roles(getRegisteredRoles(roles))
            .emailVerificationToken(emailVerificationToken)
            .build();

    if (createUserInfo != null) {
      if (createUserInfo.apply(newUser)) {
        return saveAndSendVerificationEmail(newUser, emailVerificationToken);
      }
      throw new RuntimeException("Registration failed");
    }
    return saveAndSendVerificationEmail(newUser, emailVerificationToken);
  }

  private boolean saveAndSendVerificationEmail(User user, EmailVerificationToken token) {
    User savedNewUser = userRepository.save(user);
    String verificationLink =
        emailVerificationService.generateVerificationLink(savedNewUser.getEmail(), token);

    Map<String, Object> props = new HashMap<>();
    props.put("subject", "Verification Champion League Management Account");
    props.put("body", "Please click the link below to verify your account " + verificationLink);
    return emailVerificationService.send(savedNewUser.getEmail(), props);
  }

  /**
   * Retrieves the registered roles. If the provided roles are null or empty, sets the default role
   * to USER. If roles are not null and not empty, checks if all roles exist in the database and
   * returns them, or throws a NotFoundException.
   *
   * @param roles The set of roles to be checked and retrieved.
   * @return Set<Role> The set of registered roles.
   * @throws NotFoundException If any of the specified roles is not found in the database.
   */
  @Transactional
  private Set<Role> getRegisteredRoles(Set<Role> roles) {
    return (roles == null || roles.isEmpty())
        ? Collections.singleton(getRoleByName(RoleType.USER))
        : roles.stream().map(role -> getRoleByName(role.getName())).collect(Collectors.toSet());
  }

  /**
   * Retrieves the registered roles. If the provided roles are null or empty, sets the default role
   * to USER. If roles are not null and not empty, checks if all roles exist in the database and
   * returns them, or throws a NotFoundException.
   *
   * @param role The role to be checked and retrieved.
   * @return Set<Role> The set of registered roles.
   * @throws NotFoundException If any of the specified roles is not found in the database.
   */
  @Transactional
  private Set<Role> getRegisteredRoles(Role role) {
    return Set.of(getRoleByName(role == null ? RoleType.USER : role.getName()));
  }

  @Transactional
  private Role getRoleByName(RoleType roleType) {
    return roleRepository
        .findByName(roleType)
        .orElseThrow(() -> new NotFoundException("Role not found"));
  }
}
