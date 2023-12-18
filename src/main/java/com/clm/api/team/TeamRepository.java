package com.clm.api.team;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** TeamRepository */
@Repository
public interface TeamRepository extends MongoRepository<Team, String> {

  Optional<Team> findByIdAndCreatorId(String id, String creatorId);

  Page<Team> findByTournamentIdAndStatus(
      String tournamentId, Team.Status status, Pageable pageable);

  Optional<List<Team>> findByTournamentIdAndStatus(String tournamentId, Team.Status status);

  Optional<List<Team>> findByTournamentId(String tournamentId);

  Page<Team> findByTournamentId(String tournamentId, Pageable pageable);

  Page<Team> findByCreatorId(String creatorId, Pageable pageable);

  Page<Team> findByCreatorIdAndStatus(String creatorId, Team.Status status, Pageable pageable);

  Page<Team> findByTournamentIdAndCreatorIdAndStatus(
      String tournamentId, String creatorId, Team.Status status, Pageable pageable);

  Page<Team> findByTournamentIdAndCreatorId(
      String tournamentId, String creatorId, Pageable pageable);

  boolean existsByTournamentIdAndCreatorId(String tournamentId, String creatorId);

  boolean existsByTournamentIdAndName(String tournamentId, String name);

  boolean existsByTournamentIdAndCreatorIdAndName(String tournamentId, String name);
}
