package com.clm.api.team;

import java.util.LinkedList;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/** Team */
@lombok.Getter
@lombok.Setter
@lombok.experimental.SuperBuilder
@Document(collection = "teams")
public class Team extends TeamTemplate {
  @Transient private static final long serialVersionUID = 1L;

  // the games that the team has played
  @lombok.Builder.Default private LinkedList<String> previousGameIds = new LinkedList<>();

  private String nextGameId;

  public void setNextGameId(String nextGameId) {
    this.nextGameId = nextGameId;
  }

  public String getNextGameId() {
    return nextGameId;
  }

  public String getPreviousGameId() {
    return previousGameIds.getLast();
  }

  public void addPreviousGameId(String gameId) {
    previousGameIds.add(gameId);
  }
}
