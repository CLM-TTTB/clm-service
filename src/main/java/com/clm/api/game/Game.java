package com.clm.api.game;

import com.clm.api.exceptions.business.InvalidException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.interfaces.IPatchSubject;
import com.clm.api.interfaces.IRank;
import com.clm.api.interfaces.IRankObserver;
import com.clm.api.utils.DuplicatePair;
import com.clm.api.utils.SH256Hasher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

@lombok.NoArgsConstructor
@lombok.Getter
@lombok.Setter
public class Game implements IRank, IPatchSubject {

  @Override
  public String[] getIgnoredFields() {
    return new String[] {
      "id",
      "tournamentId",
      "score",
      "rank",
      "createdAt",
      "updatedAt",
      "previousGameIds",
      "observers",
      "teams",
      "winnerId"
    };
  }

  private DuplicatePair<TeamTracker> teams;
  private String id;

  @JsonIgnore @Transient private final List<IRankObserver> observers = new ArrayList<>();

  private Instant rankingTime;

  private String description;
  private String stadium;

  @FutureOrPresent @DateTimeFormat private Instant startTime;

  private long durationMs;
  private long injuryTimeMs = 0;

  private String refereeId;
  private int winnerId = -1; // 0 or 1 or 2 (tie) or -1 (not finished)
  private Integer[] score = new Integer[] {null, null};

  public Game(DuplicatePair<TeamTracker> teams, String tournamentId) {
    this.teams = teams;
    this.id = genGameId(tournamentId);
    this.injuryTimeMs = 0;
  }

  public Game(
      DuplicatePair<TeamTracker> teams, int winnerId, int winnerGoalsFor, int winnerGoalsAgainst) {
    this(teams, null, winnerId, winnerGoalsFor, winnerGoalsAgainst);
  }

  public Game(
      DuplicatePair<TeamTracker> teams,
      String gameId,
      int winnerId,
      int winnerGoalsFor,
      int winnerGoalsAgainst) {
    this.teams = teams;
    this.id = gameId;
    this.injuryTimeMs = 0;
    setWinner(winnerId, winnerGoalsFor, winnerGoalsAgainst);
  }

  public static String genGameId(String tournamentId) {
    return SH256Hasher.hash(tournamentId + UUID.randomUUID().toString());
  }

  public boolean isEnoughTeams() {
    return teams != null && teams.isEnough();
  }

  public boolean isNoTeams() {
    return teams != null && teams.isEmpty();
  }

  public boolean isFinished() {
    return hasWinner() || isTie();
  }

  public boolean isTie() {
    return winnerId == 2;
  }

  public boolean hasWinner() {
    return winnerId == 0 || winnerId == 1;
  }

  public TeamTracker getWinner() {
    return hasWinner() ? teams.get(winnerId) : null;
  }

  public TeamTracker getLoser() {
    return hasWinner() ? teams.get(1 - winnerId) : null;
  }

  public void draw() {
    winnerId = 2;
    if (teams.hasFirst()) teams.getFirst().draw(teams.getSecond(), 0, 0);
    if (teams.hasSecond()) teams.getSecond().draw(teams.getFirst(), 0, 0);
  }

  public void setWinner(Integer id, int goalsFor, int goalsAgainst) {
    if (goalsFor < goalsAgainst)
      throw new InvalidException("goalsFor must be greater than goalsAgainst");

    if (winnerId == -1) {
      if (id == 0 || id == 1) {
        winnerId = id;
        if (teams.has(id)) {
          teams.get(id).win(teams.get(1 - id), goalsFor, goalsAgainst);
          score[id] = goalsFor;
        }
        if (teams.has(1 - id)) {
          teams.get(1 - id).lose(teams.get(id), goalsAgainst, goalsFor);
          score[1 - id] = goalsAgainst;
        }
        this.rankingTime = Instant.now();
        notifyObservers();
      } else throw new NotFoundException("id must be 0 or 1");
    } else {
      if (winnerId != id && (id == 0 || id == 1)) {
        winnerId = id;
        if (teams.has(id)) {
          score[id] = goalsFor;
          teams.get(id).setGoalDifference(goalsFor - goalsAgainst);
          if (teams.has(1 - id)) {
            score[1 - id] = goalsAgainst;
            teams.get(1 - id).setGoalDifference(goalsAgainst - goalsFor);
            int rankFirst = teams.get(id).getRank();
            teams.get(id).setRank(teams.get(1 - id).getRank());
            teams.get(1 - id).setRank(rankFirst);
            return;
          }
        }
        if (teams.has(1 - id)) {
          score[1 - id] = goalsAgainst;
          teams.get(1 - id).setGoalDifference(goalsAgainst - goalsFor);
          if (teams.has(id)) {
            score[id] = goalsFor;
            teams.get(id).setGoalDifference(goalsFor - goalsAgainst);
            int rankFirst = teams.get(id).getRank();
            teams.get(id).setRank(teams.get(1 - id).getRank());
            teams.get(1 - id).setRank(rankFirst);
            return;
          }
        }
      } else if (winnerId == id
          && (id == 0 || id == 1)
          && (score[id] != goalsFor || score[1 - id] != goalsAgainst)
          && teams.isEnough()) {
        score[id] = goalsFor;
        score[1 - id] = goalsAgainst;
        teams.get(id).setGoalDifference(goalsFor - goalsAgainst);
        teams.get(1 - id).setGoalDifference(goalsAgainst - goalsFor);
      }
    }
  }

  public void setWinner(String id, int goalsFor, int goalsAgainst) {
    if (teams.getFirst() != null && id.equals(teams.getFirst().getId()))
      setWinner(0, goalsFor, goalsAgainst);
    else if (teams.getSecond() != null && id.equals(teams.getSecond().getId()))
      setWinner(1, goalsFor, goalsAgainst);
    else throw new NotFoundException("id must be one of the two team ids");
  }

  public void setWinner(TeamTracker team, int goalsFor, int goalsAgainst) {
    if (team == null) return;
    else if (team.equals(teams.getFirst())) setWinner(0, goalsFor, goalsAgainst);
    else if (team.equals(teams.getSecond())) setWinner(1, goalsFor, goalsAgainst);
    else throw new NotFoundException("team must be one of the two teams");
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

  @Override
  public void attach(IRankObserver observer) {
    this.observers.add(observer);
  }

  @Override
  public void attach(List<? extends IRankObserver> observers) {
    this.observers.clear();
    this.observers.addAll(observers);
  }

  @Override
  public void detach(IRankObserver observer) {
    this.observers.remove(observer);
  }

  @Override
  public void detachAll() {
    this.observers.clear();
  }

  @Override
  public void notifyObservers() {
    for (IRankObserver observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public Instant getRankingTime() {
    return rankingTime == null ? Instant.EPOCH : rankingTime;
  }

  @Override
  public String toString() {
    return "Game: "
        + this.id
        + " "
        + (this.teams.hasFirst()
            ? this.teams.getFirst().getName() + " " + this.teams.getFirst().getRank()
            : "team 1 null")
        + " vs "
        + (this.teams.hasSecond()
            ? this.teams.getSecond().getName() + " " + this.teams.getSecond().getRank()
            : "team 2 null")
        + " "
        + this.winnerId;
  }
}
