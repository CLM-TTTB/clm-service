package com.clm.api.tournament;

import com.clm.api.enums.Visibility;
import com.clm.api.interfaces.CRUDService;
import com.clm.api.response.PageResponse;
import com.clm.api.team.Team;
import com.clm.api.team.Team.Status;
import java.security.Principal;
import org.springframework.data.domain.Pageable;

/** TournamentService */
public interface TournamentService extends CRUDService<Tournament, String> {

  Team addTeam(String tournamentId, Team team, Principal connectedUser);

  PageResponse<Tournament> getAll(
      Visibility visibility, Tournament.Status tournamentStatus, Pageable pageable);

  PageResponse<Team> getEnrolledTeams(String id, Status status, Pageable pageable);
}
