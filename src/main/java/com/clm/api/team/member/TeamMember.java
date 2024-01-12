package com.clm.api.team.member;

import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.team.member.player.Player;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/** Member */
@lombok.Getter
@lombok.Setter
@lombok.experimental.SuperBuilder
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "role",
    defaultImpl = Player.class)
public abstract class TeamMember {

  @JsonTypeName
  public enum Role {
    PLAYER,
    COACH,
    MANAGER
  }

  @Transient private static final long serialVersionUID = 1L;

  @Id protected String id;

  private Role role;

  @NotBlank protected String name;

  @NotNull
  @Min(value = 6, message = ErrorMessage.AGE_INVALID)
  protected Byte age;

  @lombok.Builder.Default protected String image = "";
  @lombok.Builder.Default protected String description = "";

  // NOTE: If the member of a team has the account in the system, the userId will be set.
  protected String userId;

  protected TeamMember(Role role) {
    this.image = "";
    this.description = "";
    this.role = role;
  }

  public TeamMember(TeamMember teamMember) {
    this.name = teamMember.getName();
    this.age = teamMember.getAge();
    this.image = teamMember.getImage();
    this.description = teamMember.getDescription();
    this.userId = teamMember.getUserId();
  }
}
