package com.clm.api.team;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** TeamRepository */
@Repository
public interface TeamRepository extends MongoRepository<Team, String> {}
