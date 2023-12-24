package com.clm.api.game;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.team.TeamRepository;
import com.clm.api.tournament.Tournament;
import com.clm.api.tournament.TournamentRepository;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'get'");
  }

  @Override
  public GameTracker create(GameTracker t, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'create'");
  }

  @Override
  public GameTracker update(GameTracker t, Principal connectedUser) {
    // TODO Auto-generated method stub
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
  public GameTracker schedule(String tournamentId, Principal connectedUser) {
    // TODO:
    //
    List<String> acceptedTeamIds =
        List.of(
            "team1", "team2", "team3", "team4", "team5", "team6", "team7", "team8", "team9",
            "team10", "team11", "team12", "team13", "team14", "team15");

    User user = PrincipalHelper.getUser(connectedUser);
    Tournament tournament =
        tournamentRepository
            .findByIdAndCreatorId(tournamentId, user.getId())
            .orElseThrow(() -> new NotFoundException("Tournament not found"));
    // List<Team> teams =
    //     teamRepository
    //         .findByTournamentId(tournamentId)
    //         .orElseThrow(() -> new NotFoundException("Tournament not found"));

    // GameTracker gameTracker =
    //     new KnockOutGameTracker(tournamentId, new ArrayList<>(tournament.getAcceptedTeamIds()));
    KnockOutGameTracker gameTracker =
        new KnockOutGameTracker(tournamentId, new ArrayList<>(acceptedTeamIds));
    // gameTracker.initFirstRound();
    gameTracker.createAllRounds();

    return gameTracker;
  }
}
