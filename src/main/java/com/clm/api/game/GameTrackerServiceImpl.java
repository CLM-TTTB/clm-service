package com.clm.api.game;

import com.clm.api.exceptions.business.InvalidException;
import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.team.TeamRepository;
import com.clm.api.tournament.Tournament;
import com.clm.api.tournament.TournamentRepository;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Service;

/** GameTrackerServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class GameTrackerServiceImpl implements GameTrackerService {

  private final GameTrackerRepository gameTrackerRepository;
  private final TournamentRepository tournamentRepository;
  private final TeamRepository teamRepository;

  @Override
  public GameTracker get(String id) throws NotFoundException {
    return gameTrackerRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Game Tracker not found"));
  }

  @Override
  public GameTracker create(GameTracker t, Principal connectedUser) {
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

  @Override
  public GameTracker update(GameTracker t, Principal connectedUser) {
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public GameTracker patch(
      Map<String, Object> identifyFields,
      Map<String, Object> updateFields,
      String[] ignoreFields,
      Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'patch'");
  }

  @Override
  public void delete(String id, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public GameTracker schedule(
      String tournamentId, Integer maxTeamPerTable, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    Tournament tournament =
        tournamentRepository
            .findByIdAndCreatorId(tournamentId, user.getId())
            .orElseThrow(() -> new NotFoundException("Tournament not found"));

    GameTracker gameTracker;
    switch (tournament.getCompetitionType()) {
      case KNOCKOUT:
        gameTracker =
            new KnockOutGameTracker(
                tournamentId, user.getId(), new ArrayList<>(tournament.getAcceptedTeams()));
        break;
      case ROUND_ROBIN:
        gameTracker =
            new RoundRobinGameTracker(
                tournamentId, user.getId(), new ArrayList<>(tournament.getAcceptedTeams()));

        break;
      case KNOCKOUT_WITH_ROUND_ROBIN:
        gameTracker =
            new KnockOutWithRoundRobinGameTracker(
                tournamentId,
                user.getId(),
                new ArrayList<>(tournament.getAcceptedTeams()),
                maxTeamPerTable != null ? maxTeamPerTable : 4);
        break;
      default:
        throw new InvalidException("The competition type is not supported");
    }

    gameTracker.createAllRounds();
    return gameTrackerRepository.save(gameTracker);
  }

  @Override
  public GameTracker refreshSchedule(String tournamentId, Principal connectedUser) {
    GameTracker gameTracker =
        gameTrackerRepository
            .findByTournamentId(tournamentId)
            .orElseThrow(() -> new NotFoundException("Tournament not found"));

    gameTracker.updateAllRounds();
    return gameTracker;
  }
}
