package com.clm.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/** JwtService */
@Service
@lombok.RequiredArgsConstructor
public class JwtService {

  @Value("${clm.jwt.secret-key}")
  private String JWT_SECRET_KEY;

  @Value("${clm.jwt.access-token-expiration}")
  private long ACCESS_TOKEN_EXPIRATION;

  @Value("${clm.jwt.refresh-token-expiration}")
  private long REFRESH_TOKEN_EXPIRATION;

  @Value("${clm.jwt.refresh-token-historical}")
  private long REFRESH_TOKEN_HISTORICAL;

  private final RefreshTokenRepository refreshTokenRepository;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(String username) {
    return generateToken(new HashMap<>(), username);
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(Map<String, Object> extraClaims, String username) {
    return buildToken(extraClaims, username, ACCESS_TOKEN_EXPIRATION);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, ACCESS_TOKEN_EXPIRATION);
  }

  public RefreshToken generateRefreshToken(String username) {
    return new RefreshToken(username, REFRESH_TOKEN_EXPIRATION);
  }

  public RefreshToken generateRefreshToken(UserDetails userDetails) {
    return new RefreshToken(userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION);
  }

  public long removeRefreshTokenRevokeAfterHistorical(String username) {
    return refreshTokenRepository.deleteByUsernameAndRevokedAtBefore(
        username, System.currentTimeMillis() - REFRESH_TOKEN_HISTORICAL);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    return isTokenValid(token, userDetails.getUsername());
  }

  public boolean isTokenValid(String token, String username) {
    return (username.equals(extractUsername(token))) && !isTokenExpired(token);
  }

  private String buildToken(
      Map<String, Object> extraClaims, UserDetails userDetails, long expirationMsFromNow) {
    return buildToken(
        extraClaims,
        userDetails.getUsername(),
        new Date(System.currentTimeMillis() + expirationMsFromNow));
  }

  private String buildToken(
      Map<String, Object> extraClaims, String username, long expirationMsFromNow) {
    return buildToken(
        extraClaims, username, new Date(System.currentTimeMillis() + expirationMsFromNow));
  }

  private String buildToken(Map<String, Object> extraClaims, String username, Date expiration) {
    if (expiration.before(new Date())) {
      throw new IllegalArgumentException("Expiration date must be in the future");
    }
    return Jwts.builder()
        .claims(extraClaims)
        .subject(username)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(expiration)
        .signWith(getSignInKey(), Jwts.SIG.HS256)
        .compact();
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
