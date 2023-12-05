package com.clm.api.user;

import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Role */
@lombok.Data
@lombok.Builder
@Document(collection = "roles")
public class Role {

  @Id private String id;

  private RoleType name;

  private Set<Permission> permissions;

  public Role() {}

  public Role(RoleType name) {
    this.name = name;
    this.permissions = null;
  }

  public Role(String id, RoleType name) {
    this.id = id;
    this.name = name;
    this.permissions = null;
  }

  public Role(String id, RoleType name, Set<Permission> permissions) {
    this.id = id;
    this.name = name;
    this.permissions = permissions;
  }
}
