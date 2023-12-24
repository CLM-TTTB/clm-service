package com.clm.api.game;

import com.clm.api.interfaces.IIdTracker;
import com.clm.api.interfaces.IPair;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Data
@lombok.Builder
@lombok.AllArgsConstructor
@Document(collection = "game_details")
public class GameDetail {

  @Transient private static final long serialVersionUID = 1L;

  @lombok.Getter
  @lombok.Setter
  @lombok.AllArgsConstructor
  public class TeamTracker implements IIdTracker<String> {
    private String id;
    private String uniform;

    @Override
    public String getId() {
      return id;
    }
  }

  @Id private String id;
  private String tournamentId;

  private String nextGameId;
  private String previousGameId;

  private String name;
  private String description;
  private String stadium;

  // 1 game has 2 teams
  private IPair<TeamTracker, TeamTracker> teams;
  private Integer winnerId;

  public TeamTracker getWinner() {
    return winnerId == null ? null : teams.get(winnerId);
  }

  public TeamTracker getLoser() {
    return winnerId == null ? null : teams.get(1 - winnerId);
  }

  public void setWinner(TeamTracker team) {
    if (team == null) return;
    else if (team.equals(teams.getFirst())) winnerId = 0;
    else if (team.equals(teams.getSecond())) winnerId = 1;
    else throw new IllegalArgumentException("team must be one of the two teams");
  }

  public void setWinnerId(Integer id) {
    if (id < 0 || id > 1) {
      throw new IllegalArgumentException("winnerId must be 0 or 1");
    }
    winnerId = id;
  }

  public void setWinnerId(String id) {
    if (id.equals(teams.getFirst().getId())) winnerId = 0;
    else if (id.equals(teams.getSecond().getId())) winnerId = 1;
    else throw new IllegalArgumentException("id must be one of the two teams");
  }

  @JsonIgnore
  public TeamTracker getTeam1() {
    return teams.getFirst();
  }

  public void setTeam1(TeamTracker team) {
    teams.setFirst(team);
  }

  @JsonIgnore
  public String getTeam1Id() {
    return teams.getFirst().getId();
  }

  @JsonIgnore
  public TeamTracker getTeam2() {
    return teams.getSecond();
  }

  public void setTeam2(TeamTracker team) {
    teams.setSecond(team);
  }

  @JsonIgnore
  public String getTeam2Id() {
    return teams.getSecond().getId();
  }

  private Instant startTime;
  private long durationMs;
  private long injuryTimeMs;

  // NOTE: implement this later
  private String refereeId;

  public GameDetail() {}
}
