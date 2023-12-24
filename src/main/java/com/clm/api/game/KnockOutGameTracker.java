package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import com.clm.api.game.Game.TeamTracker;
import com.clm.api.interfaces.IRoundMaker;
import com.clm.api.utils.DuplicatePair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@lombok.Getter
@lombok.Setter
public class KnockOutGameTracker extends GameTracker implements IRoundMaker {
  // gameId = tournamentId + roundIndex + gameIndex + timestamp

  private LinkedList<Round> rounds;

  public KnockOutGameTracker(String tournamentId, List<String> teamIds) {
    super(tournamentId, teamIds, CompetitionType.KNOCKOUT);
    if (teamIds == null || teamIds.size() < 2) {
      throw new UnsupportedOperationException(
          "Cannot create KnockOutGameTracker, not enough teams provided: " + teamIds.size());
    }
    this.rounds = new LinkedList<>();
  }

  public boolean hasRound() {
    return rounds != null && !rounds.isEmpty();
  }

  private void initFirstRound() {
    Round firstRound = new Round();
    ArrayList<Game> games = new ArrayList<>();

    int numberOfTeams = teamIds.size();

    for (int i = 0; i < numberOfTeams - 1; i += 2) {
      DuplicatePair<TeamTracker> teamTrackers =
          new DuplicatePair<>(
              new TeamTracker(teamIds.get(i), 0), new TeamTracker(teamIds.get(i + 1), 0));

      String gameId = Game.genGameId(this.getTournamentId(), 0, teamTrackers);
      games.add(new Game(teamTrackers, gameId));
    }

    if (numberOfTeams % 2 != 0) {
      DuplicatePair<TeamTracker> teamTrackers =
          new DuplicatePair<>(new TeamTracker(teamIds.get(numberOfTeams - 1), 0), null);

      String gameId = Game.genGameId(this.getTournamentId(), 0, teamTrackers);
      games.add(new Game(teamTrackers, gameId, 0));
    }

    firstRound.setGames(games);

    rounds.add(firstRound);
  }

  private void createNextRound() {
    if (hasRound()) {
      rounds.add(new Round(createNextRoundGames(rounds.getLast().getGames())));
    } else {
      initFirstRound();
    }
  }

  private List<Game> createNextRoundGames(List<Game> previousRoundGames) {
    List<Game> nextRoundGames = new ArrayList<>();

    int numberOfGames = previousRoundGames.size();
    for (int i = 0; i < numberOfGames - 1; i += 2) {
      DuplicatePair<TeamTracker> teamTrackers =
          new DuplicatePair<>(
              previousRoundGames.get(i).getWinner(), previousRoundGames.get(i + 1).getWinner());
      String gameId = Game.genGameId(this.getTournamentId(), rounds.size() - 1, teamTrackers);
      nextRoundGames.add(new Game(teamTrackers, gameId));
    }

    if (numberOfGames % 2 != 0) {
      DuplicatePair<TeamTracker> teamTrackers =
          new DuplicatePair<>(previousRoundGames.get(numberOfGames - 1).getWinner(), null);

      String gameId = Game.genGameId(this.getTournamentId(), rounds.size() - 1, teamTrackers);
      nextRoundGames.add(new Game(teamTrackers, gameId, 0));
    }
    return nextRoundGames;
  }

  public void updateRound(int roundIndex) {
    if (roundIndex < 0 || roundIndex >= rounds.size()) return;

    if (roundIndex == 0 && !hasRound()) {
      initFirstRound();
      return;
    }

    List<Game> games = rounds.get(roundIndex).getGames();
    for (int i = 0; i < games.size(); i++) {
      Game game = games.get(i);
      if (!game.hasWinner()) {
        if (game.getTeam1() == null) {
          int prevGameIndex = getThePreviousGameIndex(roundIndex, i, true);
          game.setTeam1(rounds.get(roundIndex - 1).getGames().get(prevGameIndex).getWinner());
        }
        if (game.getTeam2() == null) {
          int prevGameIndex = getThePreviousGameIndex(roundIndex, i, false);
          game.setTeam2(rounds.get(roundIndex - 1).getGames().get(prevGameIndex).getWinner());
        }
        if (game.isEnoughTeams() && game.getGameId() == null) {
          game.setGameId(Game.genGameId(this.getTournamentId(), roundIndex, game.getTeams()));
        }
      }
    }
  }

  public int getThePreviousGameIndex(int currRoundIndex, int gameIndex, boolean isLeftTeam) {
    if (currRoundIndex < 1 || currRoundIndex > rounds.size() - 1) {
      throw new IndexOutOfBoundsException(
          "Cannot get previous game index, round index out of bounds: "
              + currRoundIndex
              + "bounds: "
              + 1
              + " - "
              + (rounds.size() - 1));
    }

    int currRoundGamesSize = rounds.get(currRoundIndex).getGames().size();

    int leftOrRight = isLeftTeam ? 0 : 1;

    int prevRoundGamesSize = rounds.get(currRoundIndex - 1).getGames().size();
    prevRoundGamesSize = prevRoundGamesSize % 2 == 0 ? prevRoundGamesSize : prevRoundGamesSize + 1;

    return prevRoundGamesSize - (currRoundGamesSize - gameIndex) * 2 + leftOrRight;
  }

  public int getThePreviousGameIndex(int currRoundIndex, String teamId) {
    if (currRoundIndex < 1 || currRoundIndex > rounds.size() - 1) {
      throw new IndexOutOfBoundsException(
          "Cannot get previous game index, round index out of bounds: "
              + currRoundIndex
              + "bounds: "
              + 1
              + " - "
              + (rounds.size() - 1));
    }

    List<Game> currRoundGames = rounds.get(currRoundIndex).getGames();
    int currRoundGamesSize = currRoundGames.size();
    for (int i = 0; i < currRoundGamesSize; i++) {
      Game currGame = currRoundGames.get(i);
      if (currGame.getTeam1Id().equals(teamId)) {
        return getThePreviousGameIndex(currRoundIndex, i, true);
      } else if (currGame.getTeam2Id().equals(teamId)) {
        return getThePreviousGameIndex(currRoundIndex, i, false);
      }
    }

    throw new UnsupportedOperationException(
        "Cannot get previous game index, team not found: " + teamId);
  }

  @Override
  public List<Round> createAllRounds() {
    if (!hasRound()) {
      initFirstRound();
    }
    while (!rounds.getLast().isFinal()) {
      createNextRound();
    }
    return rounds;
  }

  @Override
  public List<Round> updateAllRounds() {
    if (!hasRound()) {
      createAllRounds();
    }
    while (!rounds.getLast().isFinal()) {
      updateRound(rounds.size() - 1);
    }
    return rounds;
  }
}
