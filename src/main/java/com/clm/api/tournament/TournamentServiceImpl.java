package com.clm.api.tournament;

import com.clm.api.enums.Visibility;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.exceptions.business.TeamRegistrationFailedException;
import com.clm.api.response.PageResponse;
import com.clm.api.team.Team;
import com.clm.api.team.Team.Status;
import com.clm.api.team.TeamRepository;
import com.clm.api.team.TeamService;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import java.time.Instant;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@lombok.RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
  private final TournamentRepository tournamentRepository;
  private final TeamService teamService;
  private final TeamRepository teamRepository;

  @Override
  public Tournament get(String id) {
    return tournamentRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Tournament with id " + id + " not found"));
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
    } else if (!tournament.isEnoughPlayersPerTeam(team.getMembers())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TEAM_TOO_SMALL);
    } else if (tournament.isExceedPlayersPerTeam(team.getMembers())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TEAM_TOO_BIG);
    } else if (teamRepository.existsByTournamentIdAndName(tournamentId, team.getName())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.TEAM_NAME_ALREADY_TAKEN);
    } else if (teamRepository.existsByTournamentIdAndCreatorId(tournamentId, user.getId())) {
      throw new TeamRegistrationFailedException(
          TeamRegistrationFailedException.Reason.ALREADY_ENROLLED);
    }
    // else {
    // List<Team> teams =
    //     teamRepository
    //         .findByTournamentIdAndStatus(tournamentId, Team.Status.ACCEPTED)
    //         .orElse(null);
    // if (teams != null) {
    //   if (teams.stream().anyMatch(t -> t.getCreatorId().equals(user.getId()))) {
    //     throw new TeamRegistrationFailedException(
    //         TeamRegistrationFailedException.Reason.ALREADY_ENROLLED);
    //   } else if (teams.stream().anyMatch(t -> t.getName().equals(team.getName()))) {
    //     throw new TeamRegistrationFailedException(
    //         TeamRegistrationFailedException.Reason.TEAM_NAME_ALREADY_TAKEN);
    //   }
    // }
    //   if (teamRepository.existsByTournamentIdAndName(tournamentId, team.getName())) {
    //     throw new TeamRegistrationFailedException(
    //         TeamRegistrationFailedException.Reason.TEAM_NAME_ALREADY_TAKEN);
    //   } else if (teamRepository.existsByTournamentIdAndCreatorId(tournamentId, user.getId())) {
    //     throw new TeamRegistrationFailedException(
    //         TeamRegistrationFailedException.Reason.ALREADY_ENROLLED);
    //   }
    // }

    tournament.increaseTotalEnrolledTeamsBy1();
    tournamentRepository.save(tournament);
    team.setTournamentId(tournamentId);
    team.setCreatorId(user.getId());
    return teamRepository.save(team);
  }
}
