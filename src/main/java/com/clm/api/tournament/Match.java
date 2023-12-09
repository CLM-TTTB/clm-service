package com.clm.api.tournament;

import java.time.Instant;
import org.springframework.data.annotation.Id;

/** Match */
// NOTE: Match is the head to head between two teams
public class Match {

  public class Team {
    // NOTE: this field is use to connect to the Team object if the team is already created and have
    // not deleted yet
    //
    private String id;
    private String score;
    private String penaltyScore;
  }

  @Id private String id;

  private String tournamentId;

  // NOTE: implement this later
  private String refereeId;

  private Team team1;
  private Team team2;

  private Instant startTime;
  private int totalTimeMs;
  private int injuryTimeMs;
}
