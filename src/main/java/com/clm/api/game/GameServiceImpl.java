package com.clm.api.game;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

/** GameServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class GameServiceImpl implements GameService {

  private final GameTrackerRepository gameTrackerRepository;

  @Override
  public Game patch(
      Map<String, Object> identifyFields,
      Map<String, Object> updateFields,
      Principal connectedUser) {
    if (updateFields == null || updateFields.isEmpty()) {
      throw new IllegalArgumentException("No field to update");
    }

    if (identifyFields.containsKey("gameId") && identifyFields.containsKey("tournamentId")) {
      User user = PrincipalHelper.getUser(connectedUser);
      GameTracker gameTracker =
          gameTrackerRepository
              .findByTournamentIdAndCreatorId(
                  (String) identifyFields.get("tournamentId"), user.getId())
              .orElseThrow(
                  () -> new NotFoundException("Game Tracker not found for this tournament"));
      Game game = gameTracker.getGame((String) (identifyFields.get("gameId")));

      for (String ignoreField : game.getIgnoredFields()) {
        updateFields.remove(ignoreField);
      }

      updateFields.forEach(
          (k, v) -> {
            Field field = ReflectionUtils.findField(game.getClass(), k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, game, v);
          });

      gameTrackerRepository.save(gameTracker);
      return game;
    } else {
      throw new IllegalArgumentException("No field to identify the tournament template");
    }
  }

  public Game get(String tournamentId, String gameId) {
    GameTracker gameTracker =
        gameTrackerRepository
            .findByTournamentId(tournamentId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "You not create any schedule for this tournament yet. Please"
                            + " create one first."));
    Game game = gameTracker.getGame(gameId);
    if (game == null) {
      throw new NotFoundException("Game not found. Please check if game is white visit");
    }
    game.setWinner(0);
    gameTrackerRepository.save(gameTracker);

    return game;
  }
}
