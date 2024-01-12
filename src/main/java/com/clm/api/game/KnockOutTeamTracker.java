package com.clm.api.game;

import com.clm.api.interfaces.IRank;

public class KnockOutTeamTracker extends TeamTracker {

  public KnockOutTeamTracker(String id, String name) {
    super(id, name);
  }

  @Override
  public void win() {}

  @Override
  public void lose() {}

  @Override
  public void draw() {}

  @Override
  public void update(IRank subscriber) {
    Game game = (Game) subscriber;
    if (game.hasWinner()) {

      if (game.isEnoughTeams()) {
        if (game.getWinner().getRank().equals(game.getLoser().getRank())) {
          game.getLoser().setRank(game.getLoser().getRank() + 1);
        } else if (game.getWinner().getRank() > game.getLoser().getRank()) {
          int winnerRank = game.getWinner().getRank();
          game.getWinner().setRank(game.getLoser().getRank());
          game.getLoser().setRank(winnerRank);
        }
      }
      if (game.getWinner().getId().equals(this.getId())) return;

      if (game.getLoser().getId().equals(this.getId())) return;

      if (this.rank.equals(game.getWinner().getRank())) {
        game.getLoser().setRank(game.getLoser().getRank() + 1);
      }
    }
  }

  @Override
  public void win(TeamTracker loser, int goalsFor, int goalsAgainst) {
    this.goalsFor = goalsFor;
    this.goalDifference += goalsFor - goalsAgainst;
  }

  @Override
  public void lose(TeamTracker winner, int goalsFor, int goalsAgainst) {
    this.goalsFor = goalsFor;
    this.goalDifference += goalsFor - goalsAgainst;
  }

  @Override
  public void draw(TeamTracker opponent, int goalsFor, int goalsAgainst) {
    this.goalsFor = goalsFor;
    this.goalDifference += goalsFor - goalsAgainst;
  }
}
