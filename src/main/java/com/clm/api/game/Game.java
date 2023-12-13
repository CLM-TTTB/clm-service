package com.clm.api.game;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/** Match */
// NOTE: Match is the head to head between two teams

@lombok.Data
@lombok.Builder
@Document(collection = "games")
public class Game {

  @Transient private static final long serialVersionUID = 1L;

  @lombok.Getter
  @lombok.Setter
  @lombok.AllArgsConstructor
  public class TeamTracking {
    private String teamId;
    private String uniform;
    // score of the team in the game
    private int score;
  }

  @Id private String id;
  private String nextGameId;
  private String previousGameId;
  private String tournamentId;

  private String name;
  private String description;
  private String stadium;

  // 1 game has 2 teams
  private TeamTracking team1;
  private TeamTracking team2;
  private TeamTracking winnerTeamId;

  private Instant startTime;
  private long durationMs;
  private int injuryTimeMs;

  // NOTE: implement this later
  private String refereeId;
}
