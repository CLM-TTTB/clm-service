package com.clm.api.security;

import java.util.Date;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Token */
@lombok.Getter
@lombok.Setter
@lombok.Builder
@lombok.AllArgsConstructor
@Document(collection = "refresh_tokens")
public class RefreshToken {

  @Id private String id;

  private String username;
  private String token;

  @lombok.Builder.Default private boolean revoked = false;

  private Date expiresAt;
  private Date revokedAt;

  // this is the time the token was created
  // to be used to track when the user last logged in
  @lombok.Builder.Default private Date createdAt = new Date();

  // this is the time the token was last updated
  @lombok.Builder.Default private Date updatedAt = new Date();

  public RefreshToken(String username, Date expiresAt) {
    this.revoked = false;
    this.username = username;
    this.expiresAt = expiresAt;

    this.createdAt = new Date();
    this.updatedAt = new Date();
    this.token = UUID.randomUUID().toString();
  }

  public RefreshToken(String username, long expirationTimeInMs) {
    this(username, new Date(System.currentTimeMillis() + expirationTimeInMs));
  }

  public RefreshToken() {
    this.revoked = false;
    this.createdAt = new Date();
    this.updatedAt = new Date();
    this.token = UUID.randomUUID().toString();
  }

  public boolean isExpired() {
    return expiresAt.before(new Date());
  }

  public RefreshToken updateToken() {
    this.token = UUID.randomUUID().toString();
    this.updatedAt = new Date();
    return this;
  }

  public RefreshToken revoke() {
    if (!this.revoked) {
      this.revoked = true;
      this.revokedAt = new Date();
    }

    return this;
  }
}
