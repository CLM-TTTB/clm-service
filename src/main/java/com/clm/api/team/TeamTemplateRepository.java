package com.clm.api.team;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** TeamTemplateRepository */
@Repository
public interface TeamTemplateRepository extends MongoRepository<TeamTemplate, String> {
  Optional<TeamTemplate> findByNameAndCreatorId(String name, String creatorId);

  Optional<TeamTemplate> findByIdAndCreatorId(String id, String creatorId);

  List<TeamTemplate> findByCreatorId(String creatorId);

  boolean existsByNameAndCreatorId(String name, String creatorId);

  long countByCreatorId(String creatorId);
}
