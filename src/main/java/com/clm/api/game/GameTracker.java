package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import com.clm.api.interfaces.IRoundMaker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.beans.Transient;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type")
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Document(collection = "game_trackers")
public abstract class GameTracker implements IRoundMaker {

  private boolean allowReCreated = true;

  @Id private String tournamentId;
  private String creatorId;
  private CompetitionType type;

  @JsonIgnore protected List<TeamTracker> teams;

  protected GameTracker(
      String tournamentId, String creatorId, List<TeamTracker> teams, CompetitionType type) {
    this.tournamentId = tournamentId;
    this.creatorId = creatorId;
    this.type = type;
    this.teams = teams;
  }

  @JsonIgnore
  @Transient
  public abstract List<TeamTracker> getRanks();

  public abstract Game getGame(String id);

  @JsonIgnore
  public abstract List<Game> getGames();
}
