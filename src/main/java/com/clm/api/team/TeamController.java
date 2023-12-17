package com.clm.api.team;

import java.security.Principal;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** TeamController */
@RestController
@RequestMapping("/v1/teams")
@lombok.RequiredArgsConstructor
public class TeamController {

  private final TeamService teamService;

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateTeam(
      @PathVariable("id") String id,
      @RequestBody Map<String, Object> updateFields,
      Principal connectedUser) {
    return ResponseEntity.ok(teamService.patch(Map.of("id", id), updateFields, connectedUser));
  }
}
