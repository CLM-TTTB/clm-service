package com.clm.api.team;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Map;
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

/** TeamController */
@RestController
@lombok.RequiredArgsConstructor
@RequestMapping("/v1/team-templates")
public class TeamTemplateController {

  private final TeamTemplateService teamTemplateService;
  private final TeamService teamService;

  @PostMapping("")
  public ResponseEntity<?> createNewTeamTemplate(
      @Valid @RequestBody TeamTemplate teamTemplate, Principal connectedUser) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(teamTemplateService.create(teamTemplate, connectedUser));
  }

  @GetMapping("")
  public ResponseEntity<?> getAllTeamTemplates(
      @RequestParam(defaultValue = "true") boolean nameOnly, Principal connectedUser) {
    if (!nameOnly) {
      return ResponseEntity.ok(teamTemplateService.getAll(connectedUser));
    }
    return ResponseEntity.ok(teamTemplateService.getAllTeamTemplateNames(connectedUser));
  }

  @GetMapping("/{name}")
  public ResponseEntity<?> createTeamFromTemplate(
      @PathVariable("name") String name, Principal connectedUser) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(teamService.createFromTemplate(name, connectedUser));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> updateTeamTemplate(
      @PathVariable("id") String id,
      @RequestBody Map<String, Object> updateFields,
      Principal connectedUser) {
    return ResponseEntity.ok(
        teamTemplateService.patch(Map.of("id", id), updateFields, connectedUser));
  }
}
