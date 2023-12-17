package com.clm.api.tournament;

import com.clm.api.team.Team;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** TournamentController */
@RestController
@RequestMapping("/v1/tournaments")
@lombok.RequiredArgsConstructor
public class TournamentController {

  private final TournamentService tournamentService;

  @PostMapping("")
  public ResponseEntity<?> createTounament(
      @Valid @RequestBody Tournament tournament, Principal connectedUser) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tournamentService.create(tournament, connectedUser));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getTournamentById(@PathVariable("id") String id) {
    return ResponseEntity.ok(tournamentService.get(id));
  }

  @PostMapping("/{id}/teams")
  public ResponseEntity<?> addTeamToTournament(
      @PathVariable("id") String id, @Valid @RequestBody Team team, Principal connectedUser) {
    return ResponseEntity.ok(tournamentService.addTeam(id, team, connectedUser));
  }
}
