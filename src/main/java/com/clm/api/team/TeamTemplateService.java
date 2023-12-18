package com.clm.api.team;

import com.clm.api.interfaces.CRUDService;
import java.security.Principal;
import java.util.List;

/** TeamTemplateService */
public interface TeamTemplateService extends CRUDService<TeamTemplate, String> {

  TeamTemplate saveFromRegisteredTeam(Team team, Principal connectedUser);

  List<String> getAllTeamTemplateNames(Principal connectedUser);
}
