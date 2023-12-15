package com.clm.api.team.member;

import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.team.member.player.Player;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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

  protected String currentTeamId;
  protected List<String> previousTeamIds;

  // NOTE: If the member of a team has the account in the system, the userId will be set.
  protected String userId;

  public TeamMember() {
    this.image = "";
    this.description = "";
  }

  public TeamMember(String id, String name, Byte age) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.image = "";
    this.description = "";
  }

  public TeamMember(
      String id,
      String name,
      Byte age,
      String image,
      String description,
      String currentTeamId,
      List<String> previousTeamIds) {
    this(id, name, age);
    this.image = image;
    this.description = description;
    this.currentTeamId = currentTeamId;
    this.previousTeamIds = previousTeamIds;
  }

  public TeamMember(
      String id,
      String name,
      Byte age,
      String image,
      String description,
      String currentTeamId,
      List<String> previousTeamIds,
      String userId) {
    this(id, name, age, image, description, currentTeamId, previousTeamIds);
    this.userId = userId;
  }
}
