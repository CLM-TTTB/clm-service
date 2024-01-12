package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import com.clm.api.utils.DuplicatePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.val;

@lombok.Getter
@lombok.Setter
public class KnockOutGameTracker extends GameTracker {

  private LinkedList<Round> rounds;

  public KnockOutGameTracker(String tournamentId, String creatorId, List<TeamTracker> teams) {
    super(tournamentId, creatorId, teams, CompetitionType.KNOCKOUT);
    validateTeamIds(teams);
    this.rounds = new LinkedList<>();
  }

  private void validateTeamIds(List<TeamTracker> teams) {
    if (teams == null || teams.size() < 2) {
      throw new UnsupportedOperationException(
          "Cannot create KnockOutGameTracker, not enough teams provided: " + teams.size());
    }
  }

  public boolean hasRound() {
    return rounds != null && !rounds.isEmpty();
  }

  private void initFirstRound() {
    List<Game> games = new ArrayList<>();

    int numberOfTeams = teams.size();

    for (int i = 0; i < numberOfTeams - 1; i += 2) {
      val teamTrackers = new DuplicatePair<>(teams.get(i), teams.get(i + 1));

      games.add(new Game(teamTrackers, this.getTournamentId()));
    }

    if (numberOfTeams % 2 != 0) {
      val teamTrackers = new DuplicatePair<>(teams.get(numberOfTeams - 1), null);

      games.add(new Game(teamTrackers, 0));
    }

    rounds.add(new Round(games));
  }

  private boolean createNextRound() {
    if (hasRound()) {
      final Round lastRound = rounds.getLast();
      if (lastRound.isFinal()) return false;

      final List<Game> prevRoundGames = lastRound.getGames();

      int numberOfPrevGames = prevRoundGames.size();

      List<Game> nextRoundGames = new ArrayList<>();

      for (int i = 0; i < numberOfPrevGames - 1; i += 2) {
        val teamTrackers =
            new DuplicatePair<>(
                prevRoundGames.get(i).getWinner(), prevRoundGames.get(i + 1).getWinner());
        nextRoundGames.add(new Game(teamTrackers, this.getTournamentId()));
      }

      if (numberOfPrevGames % 2 != 0) {
        val teamTrackers =
            new DuplicatePair<>(prevRoundGames.get(numberOfPrevGames - 1).getWinner(), null);

        nextRoundGames.add(new Game(teamTrackers, 0));
      }

      return rounds.add(new Round(nextRoundGames));
    } else {
      initFirstRound();
      return true;
    }
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
          int prevGameIndex = getPrevGameIndex(roundIndex, i, true);
          if (prevGameIndex != -1) {
            game.setTeam1(rounds.get(roundIndex - 1).getGames().get(prevGameIndex).getWinner());
          }
        }
        if (game.getTeam2() == null) {
          int prevGameIndex = getPrevGameIndex(roundIndex, i, false);
          if (prevGameIndex != -1) {
            game.setTeam2(rounds.get(roundIndex - 1).getGames().get(prevGameIndex).getWinner());
          }
        }
        if (game.isEnoughTeams() && game.getId() == null) {
          game.setId(Game.genGameId(this.getTournamentId()));
        }
      }
    }
  }

  public int getPrevGameIndex(int currRoundIndex, int gameIndex, boolean isLeftTeam) {
    if (currRoundIndex < 1 || currRoundIndex > rounds.size() - 1) return -1;

    int currRoundGamesSize = rounds.get(currRoundIndex).getGames().size();

    int leftOrRight = isLeftTeam ? 0 : 1;

    int prevRoundGamesSize = rounds.get(currRoundIndex - 1).getGames().size();
    prevRoundGamesSize = prevRoundGamesSize % 2 == 0 ? prevRoundGamesSize : prevRoundGamesSize + 1;

    return prevRoundGamesSize - (currRoundGamesSize - gameIndex) * 2 + leftOrRight;
  }

  public int getPrevGameIndex(int currRoundIndex, String teamId) {
    if (currRoundIndex < 1 || currRoundIndex > rounds.size() - 1) return -1;

    List<Game> currRoundGames = rounds.get(currRoundIndex).getGames();
    int currRoundGamesSize = currRoundGames.size();
    for (int i = 0; i < currRoundGamesSize; i++) {
      Game currGame = currRoundGames.get(i);
      if (currGame.getTeam1Id().equals(teamId)) {
        return getPrevGameIndex(currRoundIndex, i, true);
      } else if (currGame.getTeam2Id().equals(teamId)) {
        return getPrevGameIndex(currRoundIndex, i, false);
      }
    }
    return -1;
  }

  @Override
  public List<Round> createAllRounds() {
    if (!isAllowReCreated()) {
      if (hasRound()) return rounds;
      return new LinkedList<>();
    }

    rounds = new LinkedList<>();
    Collections.shuffle(teams);

    while (createNextRound())
      ;
    return rounds;
    // same as:
    // if (!hasRound()) {
    //   initFirstRound();
    // }
    // while (!rounds.getLast().isFinal()) {
    //   createNextRound();
    // }
    // return rounds;
  }

  @Override
  public List<Round> updateAllRounds() {
    if (!hasRound()) {
      return createAllRounds();
    }

    for (int i = 0; i < rounds.size(); i++) {
      updateRound(i);
    }
    return rounds;
  }

  @Override
  public List<TeamTracker> getRanks() {
    List<TeamTracker> ranks = new ArrayList<>(teams);
    ranks.sort((team1, team2) -> team1.getRank() - team2.getRank());
    return ranks;
  }

  @Override
  public Game getGame(String id) {
    for (Round round : rounds) {
      for (Game game : round.getGames()) {
        String gameId = game.getId();
        if (gameId != null && gameId.equals(id)) {
          return game;
        }
      }
    }
    return null;
  }

  @Override
  public List<Game> getGames() {
    List<Game> games = new ArrayList<>();
    for (Round round : rounds) {
      games.addAll(round.getGames());
    }
    return games;
  }
}
