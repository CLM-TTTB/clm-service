package com.clm.api.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/** Match */
// NOTE: Match is the head to head between two teams

@lombok.Data
@lombok.Builder
@lombok.AllArgsConstructor
@Document(collection = "games")
public class Game {

  @Transient private static final long serialVersionUID = 1L;

  @lombok.Getter
  @lombok.Setter
  @lombok.AllArgsConstructor
  public class TeamTracker {
    private String id;
    private String uniform;
  }

  @Id private String id;
  private String tournamentId;

  private String nextGameId;
  private String previousGameId;

  private String name;
  private String description;
  private String stadium;

  // 1 game has 2 teams
  @lombok.Builder.Default private TeamTracker[] teams = new TeamTracker[2];
  private Integer winnerId;

  public TeamTracker getWinner() {
    if (winnerId == null) {
      return null;
    }
    return teams[winnerId];
  }

  public TeamTracker getLoser() {
    if (winnerId == null) {
      return null;
    }
    return teams[1 - winnerId];
  }

  public void setWinner(TeamTracker team) {
    if (team == null) {
      return;
    }
    if (team.equals(teams[0])) {
      winnerId = 0;
    } else if (team.equals(teams[1])) {
      winnerId = 1;
    } else {
      throw new IllegalArgumentException("team must be one of the two teams");
    }
  }

  public void setWinnerId(Integer id) {
    if (id < 0 || id > 1) {
      throw new IllegalArgumentException("winnerId must be 0 or 1");
    }
    winnerId = id;
  }

  public void setWinnerId(String id) {
    if (id.equals(teams[0].getId())) {
      winnerId = 0;
    } else if (id.equals(teams[1].getId())) {
      winnerId = 1;
    } else {
      throw new IllegalArgumentException("winnerId must be 0 or 1");
    }
  }

  @JsonIgnore
  public TeamTracker getTeam1() {
    return teams[0];
  }

  public TeamTracker getTeam2() {
    return teams[1];
  }

  public void setTeam1(TeamTracker team) {
    teams[0] = team;
  }

  public void setTeam2(TeamTracker team) {
    teams[1] = team;
  }

  @JsonIgnore
  public String getTeam1Id() {
    return teams[0].getId();
  }

  @JsonIgnore
  public String getTeam2Id() {
    return teams[1].getId();
  }

  private Instant startTime;
  private long durationMs;
  private long injuryTimeMs;

  // NOTE: implement this later
  private String refereeId;

  public Game() {
    this.teams = new TeamTracker[2];
  }
}
