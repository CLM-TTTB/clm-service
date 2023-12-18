package com.clm.api.team;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.response.PageResponse;
import com.clm.api.team.Team.Status;
import com.clm.api.tournament.TournamentRepository;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** TeamServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

  private final TeamRepository teamRepository;
  private final TeamTemplateRepository teamTemplateRepository;
  private final TournamentRepository tournamentRepository;

  @Override
  public Team get(String id) {
    return teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team not found"));
  }

  @Override
  @Transactional
  public Team create(Team t, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    t.setCreatorId(user.getId());
    return teamRepository.save(t);
  }

  @Override
  public Team update(Team t, Principal connectedUser) {
    teamRepository.findById(t.getId()).orElseThrow(() -> new NotFoundException("Team not found"));
    return teamRepository.save(t);
  }

  @Override
  @Transactional
  public void delete(String id, Principal connectedUser) {
    teamRepository.deleteById(id);
  }

  public Team createFromTemplate(TeamTemplate template, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    template.setCreatorId(user.getId());
    return Team.from(template);
  }

  @Override
  public Team createFromTemplate(String name, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);

    return createFromTemplate(
        teamTemplateRepository
            .findByNameAndCreatorId(name, user.getId())
            .orElseThrow(() -> new NotFoundException("Team template not found")),
        connectedUser);
  }

  @Override
  public PageResponse<Team> getAll(String tournamentId, Status status, Pageable pageable) {
    if (!tournamentRepository.existsById(tournamentId)) {
      throw new NotFoundException("Tournament not found");
    }

    if (status == null) {
      return new PageResponse<>(teamRepository.findByTournamentId(tournamentId, pageable));
    }

    return new PageResponse<>(
        teamRepository.findByTournamentIdAndStatus(tournamentId, status, pageable));
  }
}
