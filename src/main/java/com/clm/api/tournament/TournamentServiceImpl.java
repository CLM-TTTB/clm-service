package com.clm.api.tournament;

import com.clm.api.enums.Visibility;
import com.clm.api.exceptions.business.InvalidException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.exceptions.business.TeamRegistrationFailedException;
import com.clm.api.response.PageResponse;
import com.clm.api.team.Team;
import com.clm.api.team.Team.Status;
import com.clm.api.team.TeamRepository;
import com.clm.api.team.TeamService;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import com.clm.api.utils.ValidationHelper;
import java.lang.reflect.Field;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

@Service
@lombok.RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
  private final TournamentRepository tournamentRepository;
  private final TeamService teamService;
  private final TeamRepository teamRepository;

  @Override
  public Tournament get(String id) throws NotFoundException {
    return tournamentRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Tournament with id " + id + " not found"));
  }

  @Override
  @Transactional
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
  public Tournament patch(
      Map<String, Object> identifyFields,
      Map<String, Object> updateFields,
      String[] ignoreFields,
      Principal connectedUser) {
    if (updateFields == null || updateFields.isEmpty()) {
      throw new IllegalArgumentException("No field to update");
    }

    if (identifyFields.containsKey("id")) {
      User user = PrincipalHelper.getUser(connectedUser);
      Tournament tournament =
          tournamentRepository
              .findByIdAndCreatorId((String) identifyFields.get("id"), user.getId())
              .orElseThrow(() -> new NotFoundException("Tournament not found"));

      if (tournament.getStatus() == Tournament.Status.ONGOING
          || tournament.getStatus() == Tournament.Status.FINISHED) {
        throw new InvalidException("Cannot update an ongoing or a finished tournament");
      } else if (updateFields.containsKey("maxTeams")
          && (int) updateFields.get("maxTeams") < tournament.getTotalAcceptedTeams()) {
        throw new InvalidException(
            "Please remove some teams before decreasing the maximum number of teams");
      }

      for (String ignoreField : ignoreFields) {
        updateFields.remove(ignoreField);
      }

      updateFields.forEach(
          (k, v) -> {
            Field field = ReflectionUtils.findField(tournament.getClass(), k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, tournament, v);
          });

      return tournamentRepository.save(ValidationHelper.validate(tournament));

    } else {
      throw new IllegalArgumentException("No field to identify the tournament template");
    }
  }

  @Override
  public void delete(String id, Principal connectedUser) {
    tournamentRepository.deleteById(id);
  }

  @Override
  public PageResponse<Tournament> getAllByCreatorId(
      Visibility visibility,
      Tournament.Status tournamentStatus,
      Principal principal,
      Pageable pageable) {
    User user = PrincipalHelper.getUser(principal);
    if (visibility == null && tournamentStatus == null) {
      return new PageResponse<>(tournamentRepository.findByCreatorId(user.getId(), pageable));
    } else if (tournamentStatus == null) {
      return new PageResponse<>(
          tournamentRepository.findByCreatorIdAndVisibility(user.getId(), visibility, pageable));
    }
    if (visibility == null) {
      switch (tournamentStatus) {
        case ONGOING:
          return new PageResponse<>(
              tournamentRepository.findByCreatorIdAndStartTimeBeforeAndEndTimeAfter(
                  user.getId(), Instant.now(), Instant.now(), pageable));
        case CANCELLED:
          return new PageResponse<>(
              tournamentRepository.findByCreatorIdAndCancelled(user.getId(), true, pageable));
        case UPCOMING:
          return new PageResponse<>(
              tournamentRepository.findByCreatorIdAndStartTimeAfter(
                  user.getId(), Instant.now(), pageable));
        case FINISHED:
          return new PageResponse<>(
              tournamentRepository.findByCreatorIdAndEndTimeBefore(
                  user.getId(), Instant.now(), pageable));
        default:
          throw new IllegalArgumentException("Invalid tournament status");
      }
    }

    switch (tournamentStatus) {
      case ONGOING:
        return new PageResponse<>(
            tournamentRepository.findByCreatorIdAndVisibilityAndStartTimeBeforeAndEndTimeAfter(
                user.getId(), visibility, Instant.now(), Instant.now(), pageable));
      case CANCELLED:
        return new PageResponse<>(
            tournamentRepository.findByCreatorIdAndVisibilityAndCancelled(
                user.getId(), visibility, true, pageable));
      case UPCOMING:
        return new PageResponse<>(
            tournamentRepository.findByCreatorIdAndVisibilityAndStartTimeAfter(
                user.getId(), visibility, Instant.now(), pageable));
      case FINISHED:
        return new PageResponse<>(
            tournamentRepository.findByCreatorIdAndVisibilityAndEndTimeBefore(
                user.getId(), visibility, Instant.now(), pageable));
      default:
        throw new IllegalArgumentException("Invalid tournament status");
    }
  }

  @Override
  public PageResponse<Tournament> getAll(
      Visibility visibility, Tournament.Status tournamentStatus, Pageable pageable) {

    if (visibility == null && tournamentStatus == null) {
      return new PageResponse<>(tournamentRepository.findAll(pageable));
    } else if (tournamentStatus == null) {
      return new PageResponse<>(tournamentRepository.findByVisibility(visibility, pageable));
    }

    switch (tournamentStatus) {
      case ONGOING:
        return new PageResponse<>(
            tournamentRepository.findByVisibilityAndStartTimeBeforeAndEndTimeAfter(
                visibility, Instant.now(), Instant.now(), pageable));
      case CANCELLED:
        return new PageResponse<>(
            tournamentRepository.findByVisibilityAndCancelled(visibility, true, pageable));
      case UPCOMING:
        return new PageResponse<>(
            tournamentRepository.findByVisibilityAndStartTimeAfter(
                visibility, Instant.now(), pageable));
      case FINISHED:
        return new PageResponse<>(
            tournamentRepository.findByVisibilityAndEndTimeBefore(
                visibility, Instant.now(), pageable));
      default:
        throw new IllegalArgumentException("Invalid tournament status");
    }
  }

  @Override
  public PageResponse<Team> getEnrolledTeams(
      String tournamentid, Status status, Pageable pageable) {
    return teamService.getAll(tournamentid, status, pageable);
  }

  @Override
  @Transactional
  public Team addTeam(String tournamentId, Team team, Principal connectedUser) {
    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new NotFoundException("Tournament not found"));

    User user = PrincipalHelper.getUser(connectedUser);
    if (tournament.isViewOnly()) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TOURNAMENT_IS_VIEW_ONLY);
    } else if (!tournament.isEnrollmentOpen()) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TOURNAMENT_ENROLLMENT_CLOSED);
    } else if (tournament.isCancelled()) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TOURNAMENT_CANCELLED);
    } else if (tournament.isFull()) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.MAX_TEAMS_REACHED);
    } else if (teamRepository.existsByTournamentIdAndName(tournamentId, team.getName())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TEAM_NAME_ALREADY_TAKEN);
    } else if (teamRepository.existsByTournamentIdAndCreatorId(tournamentId, user.getId())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.ALREADY_ENROLLED);
    }

    tournament.increaseTotalEnrolledTeamsBy1();
    tournamentRepository.save(tournament);
    team.setTournamentId(tournamentId);
    team.setCreatorId(user.getId());
    return teamRepository.save(team);
  }

  @Override
  @Transactional
  public void handleTeamRegistrationApproval(
      String tournamentId, String teamId, boolean accepted, Principal connectedUser)
      throws NotFoundException, InvalidException {

    Tournament tournament =
        tournamentRepository
            .findById(tournamentId)
            .orElseThrow(() -> new NotFoundException("Tournament not found"));

    if (tournament.isFull()) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.MAX_TEAMS_REACHED);
    }

    User user = PrincipalHelper.getUser(connectedUser);
    if (!tournament.getCreatorId().equals(user.getId())) {
      throw new NotFoundException("You are not the creator of this tournament");
    }

    Team team =
        teamRepository.findById(teamId).orElseThrow(() -> new NotFoundException("Team not found"));

    if (!team.getTournamentId().equals(tournamentId)) {
      throw new NotFoundException("Team not found in this tournament");
    } else if (!tournament.isEnoughPlayersPerTeam(team.getMembers())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TEAM_TOO_SMALL);
    } else if (tournament.isExceedPlayersPerTeam(team.getMembers())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TEAM_TOO_BIG);
    }

    if (accepted) {
      tournament.acceptTeam(team.getId());
      team.setStatus(Team.Status.ACCEPTED);
    } else {
      tournament.rejectTeam(team.getId());
      team.setStatus(Team.Status.REFUSED);
    }

    teamRepository.save(team);
    tournamentRepository.save(tournament);
  }

  @Override
  public PageResponse<Tournament> search(
      String nameQuery, Tournament.Status status, Pageable pageable) {

    if (status == null) {
      return new PageResponse<>(
          tournamentRepository.findByVisibilityAndNameContainingIgnoreCase(
              Visibility.PUBLISH, nameQuery, pageable));
    }

    switch (status) {
      case ONGOING:
        return new PageResponse<>(
            tournamentRepository
                .findByVisibilityAndNameContainingIgnoreCaseAndStartTimeBeforeAndEndTimeAfter(
                    Visibility.PUBLISH, nameQuery, Instant.now(), Instant.now(), pageable));
      case CANCELLED:
        return new PageResponse<>(
            tournamentRepository.findByVisibilityAndNameContainingIgnoreCaseAndCancelled(
                Visibility.PUBLISH, nameQuery, true, pageable));
      case UPCOMING:
        return new PageResponse<>(
            tournamentRepository.findByVisibilityAndNameContainingIgnoreCaseAndStartTimeAfter(
                Visibility.PUBLISH, nameQuery, Instant.now(), pageable));
      case FINISHED:
        return new PageResponse<>(
            tournamentRepository.findByVisibilityAndNameContainingIgnoreCaseAndEndTimeBefore(
                Visibility.PUBLISH, nameQuery, Instant.now(), pageable));
      default:
        throw new IllegalArgumentException("Invalid tournament status");
    }
  }
}
