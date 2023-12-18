package com.clm.api.game;

import com.clm.api.interfaces.IIdTracker;
import com.clm.api.utils.Pair;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Round */
@lombok.Data
@lombok.AllArgsConstructor
public class Round {
  private List<GameInfo> games;

  public Round() {
    this(new ArrayList<>());
  }

  public void addGame(GameInfo game) {
    games.add(game);
  }

  public void addGame(Pair<TeamTracker, TeamTracker> teams) {
    games.add(new GameInfo(teams));
  }

  @Transient
  @JsonIgnore
  public GameInfo getGame(int index) {
    return games.get(index);
  }

  public boolean isFinished() {
    return games == null ? false : games.stream().allMatch(GameInfo::hasWinner);
  }

  public List<IIdTracker<String>> getWinners() {
    if (games == null) return null;
    List<IIdTracker<String>> winnerGames =
        games.stream()
            .filter(GameInfo::hasWinner)
            .map(GameInfo::getWinner)
            .collect(Collectors.toList());
    return winnerGames;
  }

  @lombok.Data
  @lombok.AllArgsConstructor
  public static class GameInfo {
    private Pair<TeamTracker, TeamTracker> teams;
    private String gameId;
    private Integer winnerId;

    public GameInfo(Pair<TeamTracker, TeamTracker> teams) {
      this.teams = teams;
    }

    public GameInfo(Pair<TeamTracker, TeamTracker> teams, String gameId) {
      this.teams = teams;
      this.gameId = gameId;
    }

    public static String genGameId(
        String tournamentId, int roundIndex, Pair<TeamTracker, TeamTracker> teams) {

      return tournamentId
          + roundIndex
          + (teams.hasFirst() ? teams.getFirst().getId() : "")
          + (teams.hasSecond() ? teams.getSecond().getId() : "")
          + System.currentTimeMillis();
    }

    public boolean isEnoughTeams() {
      return teams != null && teams.isEnough();
    }

    public boolean isFinished() {
      return hasWinner();
    }

    public boolean hasWinner() {
      return winnerId != null;
    }

    public TeamTracker getWinner() {
      return winnerId == null ? null : (TeamTracker) teams.get(winnerId);
    }

    public TeamTracker getLoser() {
      return winnerId == null ? null : (TeamTracker) teams.get(1 - winnerId);
    }

    public void setWinner(TeamTracker team) {
      if (team == null) return;

      if (team.equals(teams.getFirst())) {
        winnerId = 0;
      } else if (team.equals(teams.getSecond())) {
        winnerId = 1;
      } else {
        throw new IllegalArgumentException("team must be one of the two teams");
      }
    }

    public void setWinner(Integer id) {
      if (id == 0 || id == 1) {
        winnerId = id;
      } else {
        throw new IllegalArgumentException("id must be 0 or 1");
      }
    }

    public void setWinner(String id) {
      if (teams.getFirst() != null && id.equals(teams.getFirst().getId())) {
        winnerId = 0;
      } else if (teams.getSecond() != null && id.equals(teams.getSecond().getId())) {
        winnerId = 1;
      } else {
        throw new IllegalArgumentException("id must be one of the two team ids");
      }
    }

    @JsonIgnore
    @Transient
    public TeamTracker getTeam1() {
      return teams.getFirst();
    }

    public void setTeam1(TeamTracker team) {
      teams.setFirst(team);
    }

    @JsonIgnore
    @Transient
    public String getTeam1Id() {
      return teams.getFirst().getId();
    }

    @JsonIgnore
    @Transient
    public TeamTracker getTeam2() {
      return teams.getSecond();
    }

    public void setTeam2(TeamTracker team) {
      teams.setSecond(team);
    }

    @JsonIgnore
    @Transient
    public String getTeam2Id() {
      return teams.getSecond().getId();
    }
  }

  @lombok.Getter
  @lombok.Setter
  @lombok.AllArgsConstructor
  public static class TeamTracker implements IIdTracker<String> {
    private String id;
    private Integer score;

    @Override
    public String getId() {
      return id;
    }

    @Override
    public void setId(String id) {
      this.id = id;
    }

    public TeamTracker(String id) {
      this(id, 0);
    }
  }
}
