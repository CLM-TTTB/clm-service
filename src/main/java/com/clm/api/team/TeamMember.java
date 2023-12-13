package com.clm.api.team;

import com.clm.api.constants.message.ErrorMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/** Member */
@lombok.Getter
@lombok.Setter
@lombok.experimental.SuperBuilder
// NOTE: The member of a team is not a user of the system.
public abstract class TeamMember {
  @Transient private static final long serialVersionUID = 1L;

  public static enum Role {
    PLAYER,
    COACH,
    MANAGER,
    OTHER
  }

  @Id protected String id;

  @NotNull protected String name;

  @NotNull
  @Min(value = 6, message = ErrorMessage.AGE_INVALID)
  protected Byte age;

  @lombok.Builder.Default protected String image = "";
  @lombok.Builder.Default protected String description = "";

  protected String currentTeamId;
  protected List<String> previousTeamIds;

  // NOTE: If the member of a team has the account in the system, the userId will be set.
  protected String userId;

  @NotNull protected Role role;

  public TeamMember() {
    this.image = "";
    this.description = "";
  }

  public TeamMember(String id, String name, Byte age, Role role) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.role = role;
    this.image = "";
    this.description = "";
  }

  public TeamMember(
      String id,
      String name,
      Byte age,
      Role role,
      String image,
      String description,
      String currentTeamId,
      List<String> previousTeamIds) {
    this(id, name, age, role);
    this.image = image;
    this.description = description;
    this.currentTeamId = currentTeamId;
    this.previousTeamIds = previousTeamIds;
  }

  public TeamMember(
      String id,
      String name,
      Byte age,
      Role role,
      String image,
      String description,
      String currentTeamId,
      List<String> previousTeamIds,
      String userId) {
    this(id, name, age, role, image, description, currentTeamId, previousTeamIds);
    this.userId = userId;
  }
}
