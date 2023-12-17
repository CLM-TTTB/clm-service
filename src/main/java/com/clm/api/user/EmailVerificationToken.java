package com.clm.api.user;

import com.clm.api.utils.Base64Encryption;
import java.util.Date;
import java.util.UUID;
import org.yaml.snakeyaml.util.UriEncoder;

/** EmailVerificationToken */
public class EmailVerificationToken {
  private String token;
  private String resendToken;
  @lombok.Getter private Date issuedDate;
  @lombok.Getter private Date expiryDate;
  @lombok.Setter @lombok.Getter private boolean verified;

  public EmailVerificationToken() {}

  public EmailVerificationToken(long expiryTimeFromNowMs, String email) {
    this(new Date(System.currentTimeMillis() + expiryTimeFromNowMs), email);
  }

  public EmailVerificationToken(Date expiryDate, String email) {
    Date now = new Date();
    if (expiryDate.before(now)) {
      throw new IllegalArgumentException("Expiry date must be after current date");
    }
    generateToken();
    generateResendToken(email);
    this.issuedDate = now;
    this.expiryDate = expiryDate;
  }

  public String getToken() {
    return Base64Encryption.encodeBetter(token);
  }

  public String getResendToken() {
    return resendToken;
  }

  public String refreshResendToken() {
    if (resendToken != null) {
      return generateResendToken(Base64Encryption.decodeBetter(resendToken));
    }
    return null;
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

  private String generateResendToken(String email) {
    resendToken = UriEncoder.encode(Base64Encryption.encodeBetter(email, 15));
    return resendToken;
  }
}
