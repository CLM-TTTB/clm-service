package com.clm.api.game;

import com.clm.api.interfaces.CRUDService;
import java.security.Principal;

/** GameTrackerService */
public interface GameTrackerService extends CRUDService<GameTracker, String> {

  GameTracker schedule(String tournamentId, Principal connectedUser);
}
