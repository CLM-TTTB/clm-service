package com.clm.api.team;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.team.member.TeamMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/** Team */
@lombok.Getter
@lombok.Setter
@lombok.Builder
@lombok.AllArgsConstructor
@Document(collection = "teams")
public class Team {

  @Transient private static final long serialVersionUID = 1L;

  @Id protected String id;

  protected String creatorId;

  @NotBlank protected String name;

  @Pattern(regexp = Regex.PHONE_NUMBER, message = ErrorMessage.PHONE_NUMBER_INVALID)
  @NotBlank
  protected String phoneNo;

  @lombok.Builder.Default protected String image = "";
  @lombok.Builder.Default protected String description = "";

  // Save the list of uniform images url
  protected List<String> uniforms;

  @NotNull protected List<TeamMember> members;

  private String nextGameId;

  // the games that the team has played
  @lombok.Builder.Default private LinkedList<String> previousGameIds = new LinkedList<>();

  public String getPreviousGameId() {
    return previousGameIds.getLast();
  }

  public void addPreviousGameId(String gameId) {
    previousGameIds.add(gameId);
  }

  public Team() {
    this.image = "";
    this.description = "";
  }

  public Team(TeamTemplate teamTemplate) {
    this.creatorId = teamTemplate.getCreatorId();
    this.name = teamTemplate.getName();
    this.phoneNo = teamTemplate.getPhoneNo();
    this.image = teamTemplate.getImage();
    this.description = teamTemplate.getDescription();
    this.uniforms = teamTemplate.getUniforms();
    this.members = teamTemplate.getMembers();
  }

  public static Team from(TeamTemplate teamTemplate) {
    return new Team(teamTemplate);
  }
}
