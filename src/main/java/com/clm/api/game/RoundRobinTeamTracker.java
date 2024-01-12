package com.clm.api.game;

import com.clm.api.interfaces.IRank;

@lombok.Getter
@lombok.Setter
public class RoundRobinTeamTracker extends TeamTracker {
  private int score = 0;

  public RoundRobinTeamTracker(String id, String name, int score) {
    super(id, name);
    this.score = score;
  }

  @Override
  public void win() {
    score += 3;
  }

  @Override
  public void lose() {}

  @Override
  public void draw() {
    score++;
  }

  @Override
  public void update(IRank subscriber) {
    Game game = (Game) subscriber;
    if (game.hasWinner()) {

      RoundRobinTeamTracker winner = (RoundRobinTeamTracker) game.getWinner();
      RoundRobinTeamTracker loser = (RoundRobinTeamTracker) game.getLoser();

      if (winner.getScore() > this.getScore()) {
        winner.setRank(winner.getRank() + 1);
      } else if (winner.getScore() == this.getScore()) {
        if (winner.getGoalDifference() > this.getGoalDifference())
          winner.setRank(winner.getRank() + 1);
      }
      if (loser.getScore() > this.getScore()) loser.setRank(loser.getRank() + 1);
      else if (loser.getScore() == this.getScore()) {
        if (loser.getGoalDifference() > this.getGoalDifference())
          loser.setRank(winner.getRank() + 1);
      }
    }
  }
}
