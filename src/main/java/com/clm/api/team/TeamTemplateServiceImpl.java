package com.clm.api.team;

import com.clm.api.exceptions.business.AlreadyExistsException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** TeamTemplateServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamTemplateServiceImpl implements TeamTemplateService {

  private final TeamTemplateRepository teamTemplateRepository;

  @Value("${clm.props.max-team-template}")
  private long maxTeamTemplateCount;

  @Override
  public TeamTemplate get(String id) {
    return teamTemplateRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Team not found"));
  }

  @Override
  public TeamTemplate create(TeamTemplate teamTemplate, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);

    if (teamTemplateRepository.existsByNameAndCreatorId(teamTemplate.getName(), user.getId())) {
      throw new AlreadyExistsException(
          "Team template with name '" + teamTemplate.getName() + "' already exists");
    }
    if (teamTemplateRepository.countByCreatorId(user.getId()) < maxTeamTemplateCount) {
      teamTemplate.setCreatorId(user.getId());
      return teamTemplateRepository.save(teamTemplate);

    } else {
      throw new AlreadyExistsException(
          "You have reached the maximum number of team templates: " + maxTeamTemplateCount);
    }
  }

  @Override
  public TeamTemplate update(TeamTemplate t, Principal connectedUser) {
    return teamTemplateRepository.save(t);
  }

  @Override
  public void delete(String id, Principal connectedUser) {
    teamTemplateRepository.deleteById(id);
  }
}
