package com.clm.api.tournament;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.enums.AgeGroup;
import com.clm.api.enums.CompetitionType;
import com.clm.api.enums.Visibility;
import com.clm.api.team.member.TeamMember;
import com.clm.api.team.member.player.Player;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Transient;

/** Match */
@lombok.Getter
@lombok.Setter
@lombok.Builder
@lombok.AllArgsConstructor
@ValidTournament
@Document(collection = "tournaments")
public class Tournament {
  @Transient
  public enum Status {
    UNVERIFIED,
    UPCOMING,
    ONGOING,
    FINISHED,
    CANCELLED
  }

  @Id private String id;

  @Indexed private String creatorId;

  @NotBlank private String name;

  @lombok.Builder.Default private String description = "";
  @lombok.Builder.Default private String image = "";

  @NotBlank
  @Pattern(regexp = Regex.PHONE_NUMBER, message = ErrorMessage.PHONE_NUMBER_INVALID)
  private String phoneNo;

  @NotBlank private String location;

  @lombok.Builder.Default private CompetitionType competitionType = CompetitionType.KNOCKOUT;
  @lombok.Builder.Default private Visibility visibility = Visibility.PUBLISH;

  @NotNull private AgeGroup ageGroup;

  // NOTE:
  // viewOnly is only applicable for PUBLIC matches
  // If viewOnly is true, then only the creator can add teams to the match
  @lombok.Builder.Default private boolean viewOnly = false;

  @NotNull
  @Min(value = 2, message = ErrorMessage.NUMBER_OF_PLAYERS_INVALID)
  private Integer maxPlayersPerTeam;

  @Min(value = 2, message = ErrorMessage.NUMBER_OF_PLAYERS_INVALID)
  @NotNull
  private Integer minPlayersPerTeam;

  @Min(value = 2, message = ErrorMessage.NUMBER_OF_TEAMS_INVALID)
  @NotNull
  private Integer maxTeams;

  @Min(value = 2, message = ErrorMessage.NUMBER_OF_TEAMS_INVALID)
  @NotNull
  private Integer minTeams;

  @lombok.Builder.Default private int totalEnrolledTeams = 0;

  @lombok.Builder.Default private int totalAcceptedTeams = 0;

  @lombok.Builder.Default private boolean cancelled = false;

  @NotNull @DateTimeFormat @Future private Instant startTime;
  @NotNull @DateTimeFormat @Future private Instant endTime;
  @NotNull @DateTimeFormat @Future private Instant registrationDeadline;

  @lombok.Builder.Default @CreatedDate private Instant createdAt = Instant.now();
  @lombok.Builder.Default @LastModifiedDate private Instant updatedAt = Instant.now();

  public Tournament() {
    this.description = "";
    this.image = "";
    this.competitionType = CompetitionType.KNOCKOUT;
    this.visibility = Visibility.PUBLISH;
    this.totalEnrolledTeams = 0;
    this.totalAcceptedTeams = 0;
    this.viewOnly = false;
    this.cancelled = false;
  }

  public boolean isEnoughPlayersPerTeam(List<TeamMember> members) {
    if (members == null) return false;
    return members.stream().filter(m -> m instanceof Player).count() >= minPlayersPerTeam;
  }

  public boolean isExceedPlayersPerTeam(List<TeamMember> members) {
    if (members == null) return false;
    return members.stream().filter(m -> m instanceof Player).count() > maxPlayersPerTeam;
  }

  public Status getStatus() {
    Instant now = Instant.now();
    if (cancelled) {
      return Status.CANCELLED;
    } else if (now.isBefore(startTime)) {
      return Status.UPCOMING;
    } else if (now.isBefore(endTime)) {
      return Status.ONGOING;
    } else {
      return Status.FINISHED;
    }
  }

  public void finish() {
    this.endTime = Instant.now();
  }

  public void start() {
    this.startTime = Instant.now();
  }

  public void increaseTotalEnrolledTeamsBy1() {
    totalEnrolledTeams++;
  }

  public void increaseTotalAcceptedTeamsBy1() {
    totalAcceptedTeams++;
  }

  public void decreaseTotalAcceptedTeamsBy1() {
    totalAcceptedTeams = totalAcceptedTeams > 0 ? totalAcceptedTeams - 1 : 0;
  }

  public boolean isFull() {
    return totalAcceptedTeams >= maxTeams;
  }

  public boolean isEnoughTeams() {
    return totalAcceptedTeams >= minTeams;
  }

  public boolean isEnrollmentOpen() {
    return Instant.now().isBefore(registrationDeadline);
  }

  public void cancel() {
    this.cancelled = true;
  }
}
