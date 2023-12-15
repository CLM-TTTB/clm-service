package com.clm.api.team;

import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import org.springframework.stereotype.Service;

/** TeamTemplateServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamTemplateServiceImpl implements TeamTemplateService {

  private final TeamTemplateRepository teamTemplateRepository;

  @Override
  public TeamTemplate get(String id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'get'");
  }

  @Override
  public TeamTemplate create(TeamTemplate teamTemplate, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    teamTemplate.setCreatorId(user.getId());
    return teamTemplateRepository.save(teamTemplate);
  }

  @Override
  public TeamTemplate update(TeamTemplate t, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void delete(String id, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
}
