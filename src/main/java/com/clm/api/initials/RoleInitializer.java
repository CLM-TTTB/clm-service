package com.clm.api.initials;

import com.clm.api.user.Role;
import com.clm.api.user.RoleRepository;
import com.clm.api.user.RoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** RoleInitial */
@Component
@lombok.RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

  private final RoleRepository roleRepository;

  @Override
  @Transactional
  // create all roles if not exists
  public void run(String... args) throws Exception {
    for (RoleType roleType : RoleType.values()) {
      if (!roleRepository.existsByName(roleType)) {
        roleRepository.insert(new Role(roleType));
      }
    }
  }
}
