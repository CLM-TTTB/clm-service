package com.clm.api.game;

import com.clm.api.interfaces.IRank;
import com.clm.api.user.Swapper;

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
        if (game.getWinner().getRank() == game.getLoser().getRank()) {
          game.getLoser().setRank(game.getLoser().getRank() + 1);
        } else {
          Swapper.swap(game.getWinner().getRank(), game.getLoser().getRank());
        }
      }
      if (game.getWinner().getId() == this.getId()) return;
      if (game.getLoser().getId() == this.getId()) return;

      if (this.rank == game.getWinner().getRank()) {
        game.getLoser().setRank(game.getLoser().getRank() + 1);
      }
    }
  }
}
