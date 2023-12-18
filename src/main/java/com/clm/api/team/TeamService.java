package com.clm.api.team;

import com.clm.api.interfaces.CRUDService;
import com.clm.api.response.PageResponse;
import com.clm.api.team.Team.Status;
import java.security.Principal;
import org.springframework.data.domain.Pageable;

/** TeamService */
public interface TeamService extends CRUDService<Team, String> {

  Team createFromTemplate(String name, Principal connectedUser);

  PageResponse<Team> getAll(String tournamentId, Status status, Pageable pageable);

  PageResponse<Team> getAllByCreator(Status status, Principal principal, Pageable pageable);
}
