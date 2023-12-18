package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import com.clm.api.game.Round.GameInfo;
import com.clm.api.game.Round.TeamTracker;
import com.clm.api.interfaces.IRoundMaker;
import com.clm.api.utils.Pair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** KnockOutGameTracker */
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
public class KnockOutGameTracker extends GameTracker implements IRoundMaker {
  // gameId = tournamentId + roundIndex + gameIndex + timestamp

  private LinkedList<Round> rounds;

  public KnockOutGameTracker(String tournamentId, List<String> teamIds) {
    super(tournamentId, teamIds, CompetitionType.KNOCKOUT);
    if (teamIds.size() < 2) {
      throw new UnsupportedOperationException(
          "Cannot create KnockOutGameTracker, not enough teams provided: " + teamIds.size());
    }
    this.rounds = new LinkedList<>();
  }

  public boolean hasRound() {
    return rounds != null && !rounds.isEmpty();
  }

  private void initFirstRound() {
    if (teamIds == null) {
      throw new UnsupportedOperationException("Cannot create first round, no teams provided");
    }

    Round firstRound = new Round();
    ArrayList<GameInfo> games = new ArrayList<>();

    int numberOfTeams = teamIds.size();

    if (numberOfTeams < 2) {
      throw new UnsupportedOperationException(
          "Cannot create first round, not enough teams provided: " + numberOfTeams);
    }

    for (int i = 0; i < numberOfTeams; i += 2) {
      Pair<TeamTracker, TeamTracker> teamTrackers =
          Pair.of(new TeamTracker(teamIds.get(i), 0), new TeamTracker(teamIds.get(i + 1), 0));

      String gameId = GameInfo.genGameId(this.getTournamentId(), 0, teamTrackers);
      games.add(new GameInfo(teamTrackers, gameId));
    }

    if (numberOfTeams % 2 != 0) {
      Pair<TeamTracker, TeamTracker> teamTrackers =
          Pair.of(new TeamTracker(teamIds.get(numberOfTeams - 1), 0), null);

      String gameId = GameInfo.genGameId(this.getTournamentId(), 0, teamTrackers);
      games.add(new GameInfo(teamTrackers, gameId, 0));
    }

    firstRound.setGames(games);

    if (rounds == null) rounds = new LinkedList<>();
    rounds.add(firstRound);
  }

  private void createNextRound() {
    if (!hasRound()) {
      initFirstRound();
      return;
    }
    rounds.add(new Round(createNextRoundGames(rounds.getLast().getGames())));
  }

  private List<GameInfo> createNextRoundGames(List<GameInfo> previousRoundGames) {
    List<GameInfo> nextRoundGames = new ArrayList<>();

    int numberOfGames = previousRoundGames.size();
    for (int i = 0; i < numberOfGames; i += 2) {
      Pair<TeamTracker, TeamTracker> teamTrackers =
          Pair.of(previousRoundGames.get(i).getWinner(), previousRoundGames.get(i + 1).getWinner());
      String gameId = GameInfo.genGameId(this.getTournamentId(), rounds.size() - 1, teamTrackers);
      nextRoundGames.add(new GameInfo(teamTrackers, gameId));
    }

    if (numberOfGames % 2 != 0) {
      Pair<TeamTracker, TeamTracker> teamTrackers =
          Pair.of(previousRoundGames.get(numberOfGames - 1).getWinner(), null);
      String gameId = GameInfo.genGameId(this.getTournamentId(), rounds.size() - 1, teamTrackers);
      nextRoundGames.add(new GameInfo(teamTrackers, gameId, 0));
    }
    return nextRoundGames;
  }

  public void updateRound(int roundIndex) {
    if (roundIndex < 0 || roundIndex >= rounds.size()) {
      throw new IndexOutOfBoundsException(
          "Cannot re-create round, round index out of bounds: " + roundIndex);
    }

    if (roundIndex == 0) {
      if (rounds == null || rounds.isEmpty()) {
        initFirstRound();
      }
      return;
    }

    List<GameInfo> games = rounds.get(roundIndex).getGames();
    for (int i = 0; i < games.size(); i++) {
      GameInfo game = games.get(i);
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
          game.setGameId(GameInfo.genGameId(this.getTournamentId(), roundIndex, game.getTeams()));
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

    List<GameInfo> currRoundGames = rounds.get(currRoundIndex).getGames();
    int currRoundGamesSize = currRoundGames.size();
    for (int i = 0; i < currRoundGamesSize; i++) {
      GameInfo currGame = currRoundGames.get(i);
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
    while (rounds.getLast().getGames().size() > 1) {
      createNextRound();
    }
    return rounds;
  }

  @Override
  public List<Round> updateAllRounds() {
    if (rounds == null || rounds.isEmpty()) {
      createAllRounds();
    }
    while (rounds.getLast().getGames().size() > 1) {
      updateRound(rounds.size() - 1);
    }
    return rounds;
  }
}
