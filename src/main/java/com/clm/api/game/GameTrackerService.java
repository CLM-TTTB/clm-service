package com.clm.api.game;

import java.security.Principal;
import java.util.List;

/** GameTrackerService */
public interface GameTrackerService {

  GameTracker get(String tournamentId);

  List<Game> getGameFlatList(String tournamentId);

  GameTracker schedule(String tournamentId, Integer maxTeamPerTable, Principal connectedUser);

  List<Game> scheduleAndGetGameFlatList(
      String tournamentId, Integer maxTeamPerTable, Principal connectedUser);

  GameTracker refreshSchedule(String tournamentId, Principal connectedUser);

  List<Game> refreshScheduleGamesAndGetGameFlatList(String tournamentId, Principal connectedUser);

  List<TeamTracker> getRanks(String tournamentId);
}
