package com.clm.api.team.member;

import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.team.member.player.Player;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    include = JsonTypeInfo.As.PROPERTY,
    property = "role",
    defaultImpl = Player.class)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Player.class, name = "PLAYER"),
  // @JsonSubTypes.Type(value = TrackedPlayer.class, name = "TRACKED_PLAYER"),
})
public abstract class TeamMember {
  @Transient private static final long serialVersionUID = 1L;

  @Id protected String id;

  @NotBlank protected String name;

  @NotNull
  @Min(value = 6, message = ErrorMessage.AGE_INVALID)
  protected Byte age;

  @lombok.Builder.Default protected String image = "";
  @lombok.Builder.Default protected String description = "";

  // NOTE: If the member of a team has the account in the system, the userId will be set.
  protected String userId;

  public TeamMember() {
    this.image = "";
    this.description = "";
  }

  public TeamMember(TeamMember teamMember) {
    this.name = teamMember.getName();
    this.age = teamMember.getAge();
    this.image = teamMember.getImage();
    this.description = teamMember.getDescription();
    this.userId = teamMember.getUserId();
  }

  public TeamMember(String id, String name, Byte age) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.image = "";
    this.description = "";
  }

  public TeamMember(String id, String name, Byte age, String image, String description) {
    this(id, name, age);
    this.image = image;
    this.description = description;
  }

  public TeamMember(
      String id, String name, Byte age, String image, String description, String userId) {
    this(id, name, age, image, description);
    this.userId = userId;
  }
}
