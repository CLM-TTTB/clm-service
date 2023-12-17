package com.clm.api.tournament;

import com.clm.api.interfaces.CRUDService;
import com.clm.api.team.Team;
import java.security.Principal;

/** TournamentService */
public interface TournamentService extends CRUDService<Tournament, String> {

  Team addTeam(String tournamentId, Team team, Principal connectedUser);
}
