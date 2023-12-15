package com.clm.api.tournament;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.enums.AgeGroup;
import com.clm.api.enums.CompetitionType;
import com.clm.api.enums.Visibility;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
    UPCOMING,
    ONGOING,
    FINISHED
  }

  @Id private String id;
  private String creatorId;
  @NotBlank private String name;

  @lombok.Builder.Default private String description = "";
  @lombok.Builder.Default private String image = "";

  @NotBlank
  @Pattern(regexp = Regex.PHONE_NUMBER, message = ErrorMessage.PHONE_NUMBER_INVALID)
  private String phoneNo;

  @NotBlank private String location;

  @lombok.Builder.Default private CompetitionType competitionType = CompetitionType.KNOCKOUT;
  @lombok.Builder.Default private Visibility visibility = Visibility.PUBLIC;

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

  @lombok.Builder.Default private Status status = Status.UPCOMING;

  @NotNull @DateTimeFormat @Future private Instant startTime;
  @NotNull @DateTimeFormat @Future private Instant endTime;
  @NotNull @DateTimeFormat @Future private Instant registrationDeadline;

  @lombok.Builder.Default @CreatedDate private Instant createdAt = Instant.now();
  @lombok.Builder.Default @LastModifiedDate private Instant updatedAt = Instant.now();

  public Tournament() {
    this.description = "";
    this.image = "";
    this.competitionType = CompetitionType.KNOCKOUT;
    this.visibility = Visibility.PUBLIC;
    this.viewOnly = false;
    this.status = Status.UPCOMING;
  }
}
