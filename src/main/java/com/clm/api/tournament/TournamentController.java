package com.clm.api.tournament;

import com.clm.api.enums.Visibility;
import com.clm.api.team.Team;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** TournamentController */
@RestController
@RequestMapping("/v1/tournaments")
@lombok.RequiredArgsConstructor
public class TournamentController {

  private final TournamentService tournamentService;

  @GetMapping("")
  public ResponseEntity<?> getTournaments(
      @PageableDefault(page = 0, size = 9, sort = "startTime", direction = Direction.DESC)
          Pageable pageable,
      @RequestParam(defaultValue = "PUBLISH") Visibility visibility,
      @RequestParam(required = false) Tournament.Status status) {

    return ResponseEntity.ok(tournamentService.getAll(visibility, status, pageable));
  }

  @PostMapping("")
  public ResponseEntity<?> createTounament(
      @Valid @RequestBody Tournament tournament, Principal connectedUser) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tournamentService.create(tournament, connectedUser));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateTournament(
      @PathVariable("id") String id,
      @RequestBody Map<String, Object> updateFields,
      Principal connectedUser) {

    return ResponseEntity.ok(
        tournamentService.patch(
            Map.of("id", id),
            updateFields,
            new String[] {
              "id",
              "creatorId",
              "totalEnrolledTeams",
              "totalAcceptedTeams",
              "acceptedTeams",
              "createdAt",
              "updatedAt"
            },
            connectedUser));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getTournamentById(@PathVariable("id") String id) {
    return ResponseEntity.ok(tournamentService.get(id));
  }

  // team registration to tournament
  @PostMapping("/{id}/teams")
  public ResponseEntity<?> addTeamToTournament(
      @PathVariable("id") String id, @Valid @RequestBody Team team, Principal connectedUser) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tournamentService.addTeam(id, team, connectedUser));
  }

  @GetMapping("/{id}/teams")
  public ResponseEntity<?> getEnrolledTeams(
      @PathVariable("id") String id,
      @RequestParam(required = false) Team.Status status,
      @PageableDefault(page = 0, size = 9, sort = "createdAt", direction = Direction.ASC)
          Pageable pageable) {
    return ResponseEntity.ok(tournamentService.getEnrolledTeams(id, status, pageable));
  }

  @PostMapping("/{id}/approval/teams/{teamId}")
  public ResponseEntity<?> handleTeamRegistrationApproval(
      @PathVariable("id") String id,
      @PathVariable("teamId") String teamId,
      @RequestParam boolean accepted,
      Principal connectedUser) {
    tournamentService.handleTeamRegistrationApproval(id, teamId, accepted, connectedUser);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/searcher")
  public ResponseEntity<?> search(
      @RequestParam String q,
      @RequestParam(required = false) Tournament.Status status,
      @PageableDefault(page = 0, size = 9, sort = "createdAt", direction = Direction.ASC)
          Pageable pageable) {

    return ResponseEntity.ok(tournamentService.search(q, status, pageable));
  }
}
