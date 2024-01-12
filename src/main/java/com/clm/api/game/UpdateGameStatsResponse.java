package com.clm.api.game;

import com.clm.api.team.Team;
import com.clm.api.utils.DuplicatePair;

/** UpdateGameStatsResponse */
public class UpdateGameStatsResponse {

  private DuplicatePair<Team> teams;

  private Game game;

  public UpdateGameStatsResponse(Game game, Team team1, Team team2) {
    this.game = game;
    this.teams = new DuplicatePair<Team>(team1, team2);
  }
}
