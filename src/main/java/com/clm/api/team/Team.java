package com.clm.api.team;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.team.member.TeamMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/** Team */
@lombok.Getter
@lombok.Setter
@lombok.Builder
@lombok.AllArgsConstructor
@Document(collection = "teams")
public class Team {

  public enum Status {
    PENDING,
    ACCEPTED,
    REFUSED
  }

  @Transient private static final long serialVersionUID = 1L;

  @Id private String id;

  @Indexed private String tournamentId;
  @Indexed private String creatorId;

  @lombok.Builder.Default private Status status = Status.PENDING;

  @NotBlank private String name;

  @Pattern(regexp = Regex.PHONE_NUMBER, message = ErrorMessage.PHONE_NUMBER_INVALID)
  @NotBlank
  private String phoneNo;

  @lombok.Builder.Default private String image = "";
  @lombok.Builder.Default private String description = "";

  @Size(min = 1, message = ErrorMessage.UNIFORM_LIST_EMPTY)
  @NotNull
  private List<String> uniforms;

  @Size(min = 1, message = ErrorMessage.TEAM_MEMBER_LIST_EMPTY)
  @NotNull
  private List<TeamMember> members;

  private String nextGameId;

  @CreatedDate @lombok.Builder.Default private Instant createdAt = Instant.now();
  @LastModifiedDate @lombok.Builder.Default private Instant updatedAt = Instant.now();

  // the games that the team has played
  @lombok.Builder.Default private LinkedList<String> previousGameIds = new LinkedList<>();

  public String getPreviousGameId() {
    if (previousGameIds.isEmpty()) {
      return null;
    }
    return previousGameIds.getLast();
  }

  public void addPreviousGameId(String gameId) {
    previousGameIds.add(gameId);
  }

  public Team() {
    this.image = "";
    this.description = "";
    this.status = Status.PENDING;
    this.previousGameIds = new LinkedList<>();
  }

  public Team(TeamTemplate teamTemplate) {
    this.creatorId = teamTemplate.getCreatorId();
    this.name = teamTemplate.getTeamName();
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
