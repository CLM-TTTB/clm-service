package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import com.clm.api.utils.DuplicatePair;
import java.util.List;
import lombok.val;

@lombok.Getter
@lombok.Setter
public class RoundRobinGameTracker extends GameTracker {

  private Round round;

  public RoundRobinGameTracker(String tournamentId, String creatorId, List<TeamTracker> teams) {
    super(tournamentId, creatorId, teams, CompetitionType.ROUND_ROBIN);
    validateTeamIds(teams);
    this.round = new Round();
  }

  private void validateTeamIds(List<TeamTracker> teams) {
    if (teams == null || teams.size() < 2) {
      throw new UnsupportedOperationException(
          "Cannot create KnockOutGameTracker, not enough teams provided: " + teams.size());
    }
  }

  @Override
  public List<Round> updateAllRounds() {
    if (round.getGames().isEmpty()) {
      return createAllRounds();
    }

    return List.of(round);
  }

  @Override
  public List<Round> createAllRounds() {
    List<Game> games = round.getGames();
    for (int i = 0; i < teams.size() - 1; i++) {
      for (int j = i + 1; j < teams.size(); j++) {
        val gameTrackers = new DuplicatePair<>(teams.get(i), teams.get(j));
        games.add(new Game(gameTrackers, this.getTournamentId()));
      }
    }
    return List.of(round);
  }

  @Override
  public List<TeamTracker> getRanks() {

    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRanks'");
  }

  @Override
  public Game getGame(String id) {
    for (Game game : round.getGames()) {
      String gameId = game.getId();
      if (gameId != null && gameId.equals(id)) return game;
    }
    return null;
  }

  @Override
  public List<Game> getGames() {
    return round.getGames();
  }
}
