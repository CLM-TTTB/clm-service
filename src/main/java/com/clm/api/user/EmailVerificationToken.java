package com.clm.api.user;

import com.clm.api.utils.Base64Encryption;
import java.util.Date;
import java.util.UUID;

/** EmailVerificationToken */
public class EmailVerificationToken {
  private String token;
  @lombok.Getter private Date issuedDate;
  @lombok.Getter private Date expiryDate;
  @lombok.Setter @lombok.Getter private boolean verified;

  public EmailVerificationToken() {}

  public EmailVerificationToken(long expiryTimeFromNowMs) {
    this(new Date(System.currentTimeMillis() + expiryTimeFromNowMs));
  }

  public EmailVerificationToken(Date expiryDate) {
    Date now = new Date();
    if (expiryDate.before(now)) {
      throw new IllegalArgumentException("Expiry date must be after current date");
    }
    generateToken();
    this.issuedDate = now;
    this.expiryDate = expiryDate;
  }

  public String getToken() {
    return Base64Encryption.encodeBetter(token);
  }

  public boolean isValid(String token) {
    return this.token.equals(Base64Encryption.decodeBetter(token)) && !isExpired();
  }

  public boolean isExpired() {
    return new Date().after(this.expiryDate);
  }

  private String generateToken() {
    token = UUID.randomUUID().toString();
    return Base64Encryption.encodeBetter(token);
  }
}
