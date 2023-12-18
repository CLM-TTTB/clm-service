package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = KnockOutGameTracker.class, name = "KNOCKOUT"),
  // @JsonSubTypes.Type(value = RoundRobinGameTracker.class, name = "ROUND_ROBIN"),
  // @JsonSubTypes.Type(
  //     value = KnockOutWithRoundRobinGameTracker.class,
  //     name = "KNOCKOUT_WITH_ROUND_ROBIN")
})
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@Document(collection = "game_trackers")
public abstract class GameTracker {
  private String tournamentId;
  private CompetitionType type;
  protected List<String> teamIds;

  protected GameTracker(String tournamentId, List<String> teamIds, CompetitionType type) {
    this.tournamentId = tournamentId;
    this.type = type;
    this.teamIds = teamIds;
  }

  public GameTracker(String tournamentId, List<String> teamIds) {
    this.tournamentId = tournamentId;
    this.teamIds = teamIds;
  }
}
