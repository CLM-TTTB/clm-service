package com.clm.api.team;

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

/** TeamController */
@RestController
@lombok.RequiredArgsConstructor
@RequestMapping("/v1/teams")
public class TeamController {

  private final TeamTemplateService teamTemplateService;
  private final TeamService teamService;

  @PostMapping("/templates")
  public ResponseEntity<?> createNewTeamTemplate(
      @Valid @RequestBody TeamTemplate teamTemplate, Principal connectedUser) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(teamTemplateService.create(teamTemplate, connectedUser));
  }

  @GetMapping("/templates-names")
  public ResponseEntity<?> getAllTeamTemplates(Principal connectedUser) {
    return ResponseEntity.ok(teamTemplateService.getAllTeamTemplateNames(connectedUser));
  }

  @GetMapping("/templates/{name}")
  public ResponseEntity<?> createTeamFromTemplate(
      @PathVariable("name") String name, Principal connectedUser) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(teamService.createFromTemplate(name, connectedUser));
  }
}
