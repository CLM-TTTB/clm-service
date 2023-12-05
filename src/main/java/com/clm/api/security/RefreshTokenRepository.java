package com.clm.api.security;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** RefreshTokenRepository */
@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByTokenAndUsername(String token, String username);

  Optional<List<RefreshToken>> findByUsername(String username);

  long deleteByUsernameAndRevokedAtBefore(String username, long date);
}
