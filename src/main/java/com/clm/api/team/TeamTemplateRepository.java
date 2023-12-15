package com.clm.api.team;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** TeamTemplateRepository */
@Repository
public interface TeamTemplateRepository extends MongoRepository<TeamTemplate, String> {}
