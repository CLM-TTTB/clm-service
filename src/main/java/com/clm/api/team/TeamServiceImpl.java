package com.clm.api.team;

import com.clm.api.exceptions.business.InvalidException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.response.PageResponse;
import com.clm.api.team.Team.Status;
import com.clm.api.team.member.TeamMember;
import com.clm.api.tournament.Tournament;
import com.clm.api.tournament.TournamentRepository;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import com.clm.api.utils.ValidationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

/** TeamServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final TeamRepository teamRepository;
  private final TeamTemplateRepository teamTemplateRepository;
  private final TournamentRepository tournamentRepository;

  @Override
  public Team get(String id) throws NotFoundException {
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
  public Team patch(
      Map<String, Object> identifyFields,
      Map<String, Object> updateFields,
      Principal connectedUser) {
    if (updateFields == null || updateFields.isEmpty()) {
      throw new IllegalArgumentException("No field to update");
    }

    if (identifyFields.containsKey("id")) {
      User user = PrincipalHelper.getUser(connectedUser);
      Team team =
          teamRepository
              .findByIdAndCreatorId((String) identifyFields.get("id"), user.getId())
              .orElseThrow(() -> new NotFoundException("Team not found"));

      if (team.getStatus() != Team.Status.ACCEPTED) {
        Tournament tournament =
            tournamentRepository
                .findById(team.getTournamentId())
                .orElseThrow(
                    () -> new NotFoundException("Team is not registered for this tournament"));

        if (!tournament.isEnrollmentOpen()) {
          throw new InvalidException("Tournament enrollment is closed, cannot update team");
        }

        for (String ignoreField : team.getIgnoredFields()) {
          updateFields.remove(ignoreField);
        }

        updateFields.forEach(
            (k, v) -> {
              Field field = ReflectionUtils.findField(team.getClass(), k);
              field.setAccessible(true);
              if (k.equals("members") && v instanceof List) {
                CollectionType type =
                    objectMapper
                        .getTypeFactory()
                        .constructCollectionType(List.class, TeamMember.class);
                List<TeamMember> members = objectMapper.convertValue(v, type);
                ReflectionUtils.setField(field, team, members);
              } else {
                ReflectionUtils.setField(field, team, v);
              }
            });
        return teamRepository.save(ValidationHelper.validate(team));
      } else {
        throw new InvalidException("Team is already accepted, cannot update team");
      }

    } else {
      throw new IllegalArgumentException("No field to identify the team template");
    }
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
  @Transactional
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

  @Override
  public PageResponse<Team> getAllByCreator(Status status, Principal principal, Pageable pageable) {
    User user = PrincipalHelper.getUser(principal);

    if (status == null) {
      return new PageResponse<>(teamRepository.findByCreatorId(user.getId(), pageable));
    }

    return new PageResponse<>(
        teamRepository.findByCreatorIdAndStatus(user.getId(), status, pageable));
  }
}
