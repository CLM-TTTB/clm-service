package com.clm.api.game;

import com.clm.api.interfaces.IPatchSubject;
import com.clm.api.interfaces.IRank;
import com.clm.api.interfaces.IRankObserver;
import com.clm.api.utils.DuplicatePair;
import com.clm.api.utils.SH256Hasher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Transient;

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
    };
  }

  private DuplicatePair<TeamTracker> teams;
  private String id;

  @Transient private final List<IRankObserver> observers = new ArrayList<>();

  private Instant rankingTime;

  private String description;
  private String stadium;

  private Instant startTime;
  private long durationMs;
  private long injuryTimeMs = 0;

  private String refereeId;

  private Set<String> previousGameIds;

  private int winnerId = -1; // 0 or 1 or 2 (tie) or -1 (not finished)

  public Game(DuplicatePair<TeamTracker> teams, String tournamentId) {
    this.teams = teams;
    this.id = genGameId(tournamentId);
    this.injuryTimeMs = 0;
    this.previousGameIds = new LinkedHashSet<>();
  }

  public Game(DuplicatePair<TeamTracker> teams, int winnerId) {
    this(teams, null, winnerId);
  }

  public Game(DuplicatePair<TeamTracker> teams, String gameId, int winnerId) {
    this.teams = teams;
    this.id = gameId;
    this.injuryTimeMs = 0;
    this.previousGameIds = new LinkedHashSet<>();
    setWinner(winnerId);
  }

  public void saveHistory() {
    this.previousGameIds.add(this.id);
  }

  public String getPreviousGameId() {
    if (this.previousGameIds == null || this.previousGameIds.isEmpty()) return null;
    Iterator<String> iterator = this.previousGameIds.iterator();

    String secondLastElement = null;
    String lastElement = null;

    while (iterator.hasNext()) {
      secondLastElement = lastElement;
      lastElement = iterator.next();
    }
    if (secondLastElement == null) return null;
    else return secondLastElement;
  }

  public static String genGameId(String tournamentId) {
    return SH256Hasher.hash(tournamentId + UUID.randomUUID().toString());
  }

  public boolean isEnoughTeams() {
    return teams != null && teams.isEnough();
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
    if (teams.hasFirst()) teams.getFirst().draw();
    if (teams.hasSecond()) teams.getSecond().draw();
  }

  public void setWinner(TeamTracker team) {
    if (team == null) return;
    else if (team.equals(teams.getFirst())) setWinner(0);
    else if (team.equals(teams.getSecond())) setWinner(1);
    else throw new IllegalArgumentException("team must be one of the two teams");
  }

  public void setWinner(Integer id) {
    if (id == 0 || id == 1) {
      System.out.println("winner");
      winnerId = id;
      if (teams.has(id)) teams.get(id).win();
      if (teams.has(1 - id)) teams.get(1 - id).lose();
      this.rankingTime = Instant.now();
      notifyObservers();
    } else throw new IllegalArgumentException("id must be 0 or 1");
  }

  public void setWinner(String id) {
    if (teams.getFirst() != null && id.equals(teams.getFirst().getId())) setWinner(0);
    else if (teams.getSecond() != null && id.equals(teams.getSecond().getId())) setWinner(1);
    else throw new IllegalArgumentException("id must be one of the two team ids");
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
  public void detach(IRankObserver observer) {
    this.observers.remove(observer);
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
    return "Game []";
  }
}
