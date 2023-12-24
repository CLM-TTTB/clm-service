package com.clm.api.game;

import org.springframework.data.mongodb.repository.MongoRepository;

/** GameTrackerRepository */
public interface GameTrackerRepository extends MongoRepository<GameTracker, String> {}
