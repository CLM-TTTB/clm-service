package com.clm.api.tournament;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.security.Principal;
import org.springframework.stereotype.Service;

/** TournamentService */
@Service
@lombok.RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
  private final TournamentRepository tournamentRepository;

  public Tournament createTournament(Tournament tournament, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    tournament.setCreatorId(user.getId());

    return tournamentRepository.save(tournament);
  }

  @Override
  public Tournament get(String id) {
    return tournamentRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Tournament not found"));
  }

  @Override
  public Tournament create(Tournament tournament, Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    tournament.setCreatorId(user.getId());

    return tournamentRepository.save(tournament);
  }

  @Override
  public Tournament update(Tournament tournament, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void delete(String id, Principal connectedUser) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
}
