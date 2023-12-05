package com.clm.api.user;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** RoleRepository */
@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

  Optional<Role> findByName(RoleType name);

  Boolean existsByName(RoleType name);
}
