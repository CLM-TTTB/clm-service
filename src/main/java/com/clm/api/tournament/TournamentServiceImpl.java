package com.clm.api.tournament;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.team.Team;
import com.clm.api.team.TeamService;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import org.springframework.stereotype.Service;

@Service
@lombok.RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
  private final TournamentRepository tournamentRepository;
  private final TeamService teamService;

  @Override
  public Tournament get(String id) {
    return tournamentRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Tournament not found"));
  }

  @Override
  public Tournament create(Tournament tournament, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    tournament.setCreatorId(user.getId());

    return tournamentRepository.save(tournament);
  }

  @Override
  public Tournament update(Tournament tournament, Principal connectedUser) {
    tournamentRepository
        .findById(tournament.getId())
        .orElseThrow(() -> new NotFoundException("Tournament not found"));
    return tournamentRepository.save(tournament);
  }

  @Override
  public void delete(String id, Principal connectedUser) {
    tournamentRepository.deleteById(id);
  }

  @Override
  public Team addTeam(String tournamentId, Team team, Principal connectedUser) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new NotFoundException("Tournament not found");
    }

    team.setTournamentId(tournamentId);
    return teamService.create(team, connectedUser);
  }
}
