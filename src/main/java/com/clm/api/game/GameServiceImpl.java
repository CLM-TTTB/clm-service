package com.clm.api.game;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import com.clm.api.utils.PrincipalHelper;
import com.clm.api.utils.ValidationHelper;
import java.lang.reflect.Field;
import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

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

      return update(
          (String) identifyFields.get("tournamentId"),
          (String) identifyFields.get("gameId"),
          (game, gameTracker) -> {
            for (String ignoreField : game.getIgnoredFields()) {
              updateFields.remove(ignoreField);
            }

            updateFields.forEach(
                (k, v) -> {
                  if (k == "startTime") {
                    v = Instant.parse((String) v);
                  }
                  Field field = ReflectionUtils.findField(game.getClass(), k);

                  field.setAccessible(true);
                  ReflectionUtils.setField(field, game, v);
                });
            return ValidationHelper.validate(game);
          },
          connectedUser);

    } else {
      throw new IllegalArgumentException("No field to identify the tournament template");
    }
  }

  @Override
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
    return game;
  }

  private Game update(
      String tournamentId,
      String gameId,
      BiFunction<Game, GameTracker, Game> updateFunction,
      Principal connectedUser) {
    User user = PrincipalHelper.getUser(connectedUser);
    GameTracker gameTracker =
        gameTrackerRepository
            .findByTournamentIdAndCreatorId(tournamentId, user.getId())
            .orElseThrow(() -> new NotFoundException("No game tracker found for this tournament"));

    Game game = gameTracker.getGame(gameId);
    if (game == null) {
      throw new NotFoundException("Game not found. Please check if game is white visit");
    }

    Game gameUpdated = updateFunction.apply(game, gameTracker);
    gameTrackerRepository.save(gameTracker);
    return gameUpdated;
  }

  @Override
  public Game updateStats(
      String tournamentId,
      String gameId,
      String winnerTeamId,
      int winnerGoalsFor,
      int winnerGoalsAgainst,
      Principal connectedUser)
      throws NotFoundException {

    return update(
        tournamentId,
        gameId,
        (game, gameTracker) -> {
          game.attach(gameTracker.getTeams());
          game.setWinner(winnerTeamId, winnerGoalsFor, winnerGoalsAgainst);
          for (TeamTracker teamTracker : gameTracker.getTeams()) {
            if (teamTracker.getId().equals(game.getWinner().getId())) {
              teamTracker.setRank(game.getWinner().getRank());
            } else if (teamTracker.getId().equals(game.getLoser().getId())) {
              teamTracker.setRank(game.getLoser().getRank());
            }
          }
          return game;
        },
        connectedUser);
  }
}
