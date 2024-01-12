package com.clm.api.game;

import java.security.Principal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@lombok.RequiredArgsConstructor
@RequestMapping("v1/tournaments/{tournamentId}/games")
public class GameController {

  private final GameService gameService;

  @GetMapping("/{gameId}/detail")
  public ResponseEntity<?> getGameById(
      @PathVariable("tournamentId") String tournamentId, @PathVariable("gameId") String gameId) {
    return ResponseEntity.ok(gameService.get(tournamentId, gameId));
  }

  @PatchMapping("/id")
  public ResponseEntity<?> updateGameDetail(
      @PathVariable("id") String id,
      @RequestBody Map<String, Object> updateFields,
      Principal connectedUser) {
    return ResponseEntity.ok(gameService.patch(Map.of("id", id), updateFields, connectedUser));
  }

  @PatchMapping("/{gameId}/winner/{teamId}")
  public ResponseEntity<?> updateWinnerTeam(
      @PathVariable("tournamentId") String tournamentId,
      @PathVariable("gameId") String gameId,
      @PathVariable("teamId") String teamId,
      @RequestParam int winnerGoalsFor,
      @RequestParam int winnerGoalsAgainst,
      Principal principal) {
    return ResponseEntity.ok(
        gameService.updateStats(
            tournamentId, gameId, teamId, winnerGoalsFor, winnerGoalsAgainst, principal));
  }
}