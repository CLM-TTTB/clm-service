package com.clm.api.game;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** GameTrackerController */
@lombok.RequiredArgsConstructor
@RequestMapping("/v1/schedule")
@RestController
public class GameTrackerController {

  public final GameTrackerService gameTrackerService;

  @GetMapping("/{tournamentId}")
  public ResponseEntity<?> scheduleAllGames(
      @PathVariable("tournamentId") String tournamentId, Principal connectedUser) {
    return ResponseEntity.ok(gameTrackerService.schedule(tournamentId, connectedUser));
  }
}
