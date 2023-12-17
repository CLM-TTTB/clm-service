package com.clm.api.team;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import org.springframework.stereotype.Service;

/** TeamServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

  private final TeamRepository teamRepository;

  @Override
  public Team get(String id) {
    return teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team not found"));
  }

  @Override
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
  public void delete(String id, Principal connectedUser) {
    teamRepository.deleteById(id);
  }
}
