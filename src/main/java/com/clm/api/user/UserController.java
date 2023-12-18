package com.clm.api.user;

import com.clm.api.enums.Visibility;
import com.clm.api.team.Team;
import com.clm.api.team.TeamService;
import com.clm.api.tournament.Tournament;
import com.clm.api.tournament.TournamentService;
import java.security.Principal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** UserController */
@RestController
@lombok.RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

  private final UserService userService;
  private final TeamService teamService;
  private final TournamentService tournamentService;

  @PatchMapping("/change-password")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {
    userService.changePassword(request, connectedUser);
  }

  @GetMapping("/registered-teams")
  public ResponseEntity<?> getAllRegisteredTeamsOfUser(
      @PageableDefault(page = 0, size = 9, sort = "createAt", direction = Direction.DESC)
          Pageable pageable,
      @RequestParam(required = false) Team.Status status,
      Principal connectedUser) {

    return ResponseEntity.ok(teamService.getAllByCreator(status, connectedUser, pageable));
  }

  @GetMapping("/created-tournaments")
  public ResponseEntity<?> getCreatedTournaments(
      @PageableDefault(page = 0, size = 9, sort = "startTime", direction = Direction.DESC)
          Pageable pageable,
      @RequestParam(required = false) Visibility visibility,
      @RequestParam(required = false) Tournament.Status status,
      Principal connectedUser) {

    return ResponseEntity.ok(
        tournamentService.getAllByCreatorId(visibility, status, connectedUser, pageable));
  }
}
