package com.clm.api.game;

import java.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** GameTrackerController */
@lombok.RequiredArgsConstructor
@RestController
@RequestMapping("/v1/schedule")
public class GameTrackerController {

  public final GameTrackerService gameTrackerService;

  @PostMapping("/tree/{tournamentId}")
  public ResponseEntity<?> scheduleGames(
      @RequestParam(defaultValue = "4") Integer maxTeamPerTable,
      @PathVariable("tournamentId") String tournamentId,
      Principal connectedUser) {
    return ResponseEntity.ok(
        gameTrackerService.schedule(tournamentId, maxTeamPerTable, connectedUser));
  }

  @PostMapping("/{tournamentId}")
  public ResponseEntity<?> scheduleGameAndGetGameFlatList(
      @RequestParam(defaultValue = "4") Integer maxTeamPerTable,
      @PathVariable("tournamentId") String tournamentId,
      Principal connectedUser) {
    return ResponseEntity.ok(
        gameTrackerService.scheduleAndGetGameFlatList(
            tournamentId, maxTeamPerTable, connectedUser));
  }

  @PatchMapping("/tree/{tournamentId}")
  public ResponseEntity<?> refreshScheduleGames(
      @PathVariable("tournamentId") String tournamentId, Principal connectedUser) {
    return ResponseEntity.ok(gameTrackerService.refreshSchedule(tournamentId, connectedUser));
  }

  @PatchMapping("/{tournamentId}")
  public ResponseEntity<?> refreshScheduleGamesAndGetGameFlatList(
      @PathVariable("tournamentId") String tournamentId, Principal connectedUser) {
    return ResponseEntity.ok(
        gameTrackerService.refreshScheduleGamesAndGetGameFlatList(tournamentId, connectedUser));
  }
}
