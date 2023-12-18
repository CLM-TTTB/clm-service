package com.clm.api.team;

import com.clm.api.exceptions.business.AlreadyExistsException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** TeamTemplateServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamTemplateServiceImpl implements TeamTemplateService {

  private final TeamTemplateRepository teamTemplateRepository;

  @Value("${clm.props.max-team-template}")
  private long MAX_TEAM_TEMPLATE;

  @Override
  public TeamTemplate get(String id) {
    return teamTemplateRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Team not found"));
  }

  @Override
  @Transactional
  public TeamTemplate create(TeamTemplate teamTemplate, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);

    if (teamTemplateRepository.existsByNameAndCreatorId(teamTemplate.getName(), user.getId())) {
      throw new AlreadyExistsException(
          "Team template with name '" + teamTemplate.getName() + "' already exists");
    }
    if (teamTemplateRepository.countByCreatorId(user.getId()) < MAX_TEAM_TEMPLATE) {
      teamTemplate.setCreatorId(user.getId());
      return teamTemplateRepository.save(teamTemplate);

    } else {
      throw new AlreadyExistsException(
          "You have reached the maximum number of team templates: " + MAX_TEAM_TEMPLATE);
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

  @Override
  public TeamTemplate saveFromRegisteredTeam(Team team, Principal connectedUser) {
    return create(TeamTemplate.from(team), connectedUser);
  }

  @Override
  public List<String> getAllTeamTemplateNames(Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    return teamTemplateRepository
        .findByCreatorId(user.getId())
        .map((teamTemplates -> teamTemplates.stream().map(TeamTemplate::getName).toList()))
        .orElseThrow(() -> new NotFoundException("No team template found"));
  }
}
