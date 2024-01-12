package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import java.util.ArrayList;
import java.util.List;

@lombok.Getter
@lombok.Setter
public class KnockOutWithRoundRobinGameTracker extends GameTracker {

  private int teamsPerTable;
  private List<RoundRobinGameTracker> roundRobin;
  private KnockOutGameTracker knockOut;

  private void validateTeamIds(List<TeamTracker> teams) {
    if (teams == null || teams.size() < 2) {
      throw new UnsupportedOperationException(
          "Cannot create KnockOutWithRoundRobinGameTracker, not enough teams provided: "
              + teams.size());
    }
  }

  public KnockOutWithRoundRobinGameTracker(
      String tournamentId, String creatorId, List<TeamTracker> teams, int teamsPerTable) {
    super(tournamentId, creatorId, teams, CompetitionType.KNOCKOUT_WITH_ROUND_ROBIN);
    validateTeamIds(teams);
    this.teamsPerTable = teamsPerTable;
    this.roundRobin = new ArrayList<>();
  }

  private void createRoundRobin() {
    roundRobin.clear();

    LeagueTables.from(this.teams, teamsPerTable).stream()
        .map(
            table ->
                new RoundRobinGameTracker(
                    this.getTournamentId(), this.getCreatorId(), table.getTeams()))
        .forEach(roundRobin::add);

    roundRobin.forEach(RoundRobinGameTracker::createAllRounds);
  }

  private void createKnockOut() {
    // knockOut = new KnockOutGameTracker(this.getTournamentId(), roundRobin);
    // knockOut.createAllRounds();
  }

  @Override
  public List<Round> updateAllRounds() {
    return null;
  }

  @Override
  public List<Round> createAllRounds() {
    createRoundRobin();
    createKnockOut();
    return null;
  }

  @Override
  public List<TeamTracker> getRanks() {
    return null;
  }

  @Override
  public Game getGame(String id) {
    return null;
  }

  @Override
  public List<Game> getGames() {
    return null;
  }
}
