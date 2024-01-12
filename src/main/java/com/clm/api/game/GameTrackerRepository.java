package com.clm.api.game;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** GameTrackerRepository */
@Repository
public interface GameTrackerRepository extends MongoRepository<GameTracker, String> {

  Optional<GameTracker> findByTournamentId(String tournamentId);

  Optional<GameTracker> findByTournamentIdAndCreatorId(String tournamentId, String creatorId);
}
