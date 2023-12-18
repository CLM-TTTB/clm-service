package com.clm.api.team;

import com.clm.api.exceptions.business.AlreadyExistsException;
import com.clm.api.exceptions.business.InvalidException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.team.member.TeamMember;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import com.clm.api.utils.ValidationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

/** TeamTemplateServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamTemplateServiceImpl implements TeamTemplateService {
  private static final ObjectMapper objectMapper = new ObjectMapper();

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
      throw new InvalidException(
          "You have reached the maximum number of team templates: " + MAX_TEAM_TEMPLATE);
    }
  }

  @Override
  @Transactional
  public TeamTemplate update(TeamTemplate t, Principal connectedUser) {
    return teamTemplateRepository.save(t);
  }

  @Override
  @Transactional
  public TeamTemplate patch(
      Map<String, Object> identifyFields,
      Map<String, Object> updateFields,
      String[] ignoreFields,
      Principal connectedUser) {
    if (updateFields == null || updateFields.isEmpty()) {
      throw new IllegalArgumentException("No field to update");
    }

    if (identifyFields.containsKey("id")) {
      User user = PrincipalHelper.getUser(connectedUser);
      TeamTemplate template =
          teamTemplateRepository
              .findByIdAndCreatorId((String) identifyFields.get("id"), user.getId())
              .orElseThrow(() -> new NotFoundException("Team template not found"));

      for (String ignoreField : ignoreFields) {
        updateFields.remove(ignoreField);
      }

      if (updateFields.containsKey("name")
          && teamTemplateRepository.existsByNameAndCreatorId(
              (String) updateFields.get("name"), user.getId())) {
        throw new AlreadyExistsException(
            "Team template with name '" + updateFields.get("name") + "' already exists");
      }

      updateFields.forEach(
          (k, v) -> {
            Field field = ReflectionUtils.findField(template.getClass(), k);
            field.setAccessible(true);
            if (k.equals("members") && v instanceof List) {
              CollectionType type =
                  objectMapper
                      .getTypeFactory()
                      .constructCollectionType(List.class, TeamMember.class);
              List<TeamMember> members = objectMapper.convertValue(v, type);
              ReflectionUtils.setField(field, template, members);
            } else {
              ReflectionUtils.setField(field, template, v);
            }
          });

      return teamTemplateRepository.save(ValidationHelper.validate(template));

    } else {
      throw new IllegalArgumentException("No field to identify the team template");
    }
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
    return teamTemplateRepository.findByCreatorId(user.getId()).stream()
        .map(TeamTemplate::getName)
        .toList();
  }

  @Override
  public List<TeamTemplate> getAll(Principal connectedUser) {
    return teamTemplateRepository.findByCreatorId(PrincipalHelper.getUser(connectedUser).getId());
  }
}
