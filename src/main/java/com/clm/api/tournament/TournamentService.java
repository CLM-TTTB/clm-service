package com.clm.api.tournament;

import com.clm.api.enums.Visibility;
import com.clm.api.exceptions.business.InvalidException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.exceptions.business.TeamRegistrationFailedException;
import com.clm.api.interfaces.CRUDService;
import com.clm.api.response.PageResponse;
import com.clm.api.team.Team;
import com.clm.api.team.Team.Status;
import java.security.Principal;
import org.springframework.data.domain.Pageable;

/** TournamentService */
public interface TournamentService extends CRUDService<Tournament, String> {

  PageResponse<Tournament> getAll(
      Visibility visibility, Tournament.Status tournamentStatus, Pageable pageable);

  PageResponse<Tournament> getAllByCreatorId(
      Visibility visibility,
      Tournament.Status tournamentStatus,
      Principal principal,
      Pageable pageable);

  Team addTeam(String tournamentId, Team team, Principal connectedUser)
      throws TeamRegistrationFailedException;

  PageResponse<Team> getEnrolledTeams(String id, Status status, Pageable pageable);

  void handleTeamRegistrationApproval(
      String tournamentId, String teamId, boolean approved, Principal connectedUser)
      throws NotFoundException, InvalidException;

  PageResponse<Tournament> search(String nameQuery, Tournament.Status status, Pageable pageable);
}
