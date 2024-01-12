package com.clm.api.game;

import com.clm.api.exceptions.business.NotFoundException;
import java.security.Principal;
import java.util.Map;

/** GameService */
public interface GameService {

  Game get(String tournamentId, String gameId) throws NotFoundException;

  Game updateStats(
      String tournamentId,
      String gameId,
      String winnerTeamId,
      int winnerGoalsFor,
      int winnerGoalsAgainst,
      Principal connectedUser)
      throws NotFoundException;

  Game patch(
      Map<String, Object> identifyFields,
      Map<String, Object> updateFields,
      Principal connectedUser);
}
