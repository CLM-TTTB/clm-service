package com.clm.api.team;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.interfaces.IPatchSubject;
import com.clm.api.team.member.TeamMember;
import com.clm.api.team.member.player.Player;
import com.clm.api.team.member.player.TrackedPlayer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import java.util.ArrayList;
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
@lombok.experimental.SuperBuilder
@Document(collection = "teams")
public class Team implements IPatchSubject {

  public enum Status {
    PENDING,
    ACCEPTED,
    REFUSED,
  }

  @Override
  public String[] getIgnoredFields() {
    return new String[] {
      "id", "creatorId", "tournamentId", "status", "createdAt", "updatedAt", "previousGameIds"
    };
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

  @lombok.Builder.Default private List<String> uniforms = new ArrayList<>();

  @lombok.Builder.Default private List<TeamMember> members = new ArrayList<>();

  private String nextGameId;

  @CreatedDate @lombok.Builder.Default private Instant createdAt = Instant.now();
  @LastModifiedDate @lombok.Builder.Default private Instant updatedAt = Instant.now();

  // the games that the team has playeif (accepted == 0)
  @lombok.Builder.Default private LinkedList<String> previousGameIds = new LinkedList<>();

  public String getPreviousGameId() {
    if (previousGameIds == null || previousGameIds.isEmpty()) {
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
    this.members = new ArrayList<>();
    this.uniforms = new ArrayList<>();
  }

  public Team(TeamTemplate teamTemplate) {
    // this();
    this.creatorId = teamTemplate.getCreatorId();
    this.name = teamTemplate.getTeamName();
    this.phoneNo = teamTemplate.getPhoneNo();
    this.image = teamTemplate.getImage();
    this.description = teamTemplate.getDescription();
    this.uniforms = teamTemplate.getUniforms();
    this.members = teamTemplate.getMembers();
    this.status = Status.PENDING;
    this.uniforms = new ArrayList<>();
    this.previousGameIds = new LinkedList<>();
  }

  public void trackPlayer() {
    if (members == null || members.isEmpty()) {
      return;
    }

    for (int i = 0; i < members.size(); i++) {
      TeamMember member = members.get(i);
      if (member instanceof Player) {
        members.set(i, new TrackedPlayer((Player) member));
      }
    }
  }

  public static Team from(TeamTemplate teamTemplate) {
    return new Team(teamTemplate);
  }
}
