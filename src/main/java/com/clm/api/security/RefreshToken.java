package com.clm.api.security;

import java.util.Date;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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

  @lombok.Builder.Default @CreatedDate private Date createdAt = new Date();
  @lombok.Builder.Default @LastModifiedDate private Date updatedAt = new Date();

  public RefreshToken() {
    this.revoked = false;
    this.token = UUID.randomUUID().toString();
  }

  public RefreshToken(String username, Date expiresAt) {
    this();
    this.username = username;
    this.expiresAt = expiresAt;
  }

  public RefreshToken(String username, long expirationTimeInMs) {
    this(username, new Date(System.currentTimeMillis() + expirationTimeInMs));
  }

  public boolean isExpired() {
    return expiresAt.before(new Date());
  }

  public RefreshToken updateToken() {
    this.token = UUID.randomUUID().toString();
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
