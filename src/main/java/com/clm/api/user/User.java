package com.clm.api.user;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.exceptions.business.UserUnverifiedException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@lombok.Data
@lombok.Builder
@lombok.AllArgsConstructor
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {

  public enum Status {
    ACTIVE,
    BANNED,
    ARCHIVED,
    DELETED,
    COMPROMISED,
    EMAIL_UNVERIFIED,
    UNKNOWN,
  };

  @Id private String id;

  @Email(message = ErrorMessage.EMAIL_INVALID)
  @NotBlank(message = ErrorMessage.EMAIL_REQUIRED)
  @Indexed
  private String email;

  @NotBlank(message = ErrorMessage.EMAIL_REQUIRED)
  @Pattern(regexp = Regex.PASSWORD, message = ErrorMessage.PASSWORD_REQUIRED)
  @JsonIgnore
  private String password;

  @lombok.Builder.Default private String name = "User" + System.currentTimeMillis();

  @lombok.Builder.Default private String avatar = "";

  @lombok.Builder.Default @CreatedDate private Instant createdAt = Instant.now();
  @lombok.Builder.Default @LastModifiedDate private Instant updateAt = Instant.now();

  @lombok.Builder.Default private Status status = Status.EMAIL_UNVERIFIED;

  @JsonIgnore @DBRef @lombok.Builder.Default private Set<Role> roles = new HashSet<>();
  @JsonIgnore @Transient private Collection<? extends GrantedAuthority> authorities;

  public User() {
    this.avatar = "";
    this.name = "User" + System.currentTimeMillis();
    this.status = Status.EMAIL_UNVERIFIED;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (roles == null) {
      return Collections.emptySet();
    } else {
      if (authorities == null || authorities.isEmpty()) {
        Collection<SimpleGrantedAuthority> auths = new HashSet<>();
        roles.forEach(
            role -> {
              auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
              if (role.getPermissions() != null) {
                role.getPermissions()
                    .forEach(
                        permission -> {
                          auths.add(new SimpleGrantedAuthority(permission.name()));
                        });
              }
            });
        authorities = auths;
      }
      return authorities;
    }
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return status != Status.BANNED;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    if (status == Status.EMAIL_UNVERIFIED) throw new UserUnverifiedException(status);
    return status == Status.ACTIVE;
  }
}
