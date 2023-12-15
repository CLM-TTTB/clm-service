package com.clm.api.tournament;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** TounamentRepository */
@Repository
public interface TournamentRepository extends MongoRepository<Tournament, String> {}
