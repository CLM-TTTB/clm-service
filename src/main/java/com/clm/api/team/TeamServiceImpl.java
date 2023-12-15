package com.clm.api.team;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.interfaces.CRUDService;
import java.security.Principal;
import org.springframework.stereotype.Service;

/** TeamServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamServiceImpl implements CRUDService<Team, String> {

  private final TeamRepository teamRepository;

  @Override
  public Team get(String id) {
    return teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team not found"));
  }

  @Override
  public Team create(Team t, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

  @Override
  public Team update(Team t, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void delete(String id, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
}
