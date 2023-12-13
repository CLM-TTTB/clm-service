package com.clm.api.card;

import java.time.Instant;
import org.springframework.data.annotation.Transient;

/** YellowCard */
@lombok.Getter
@lombok.Setter
public class YellowCard extends Card {

  @Transient private static final long serialVersionUID = 1L;

  // The number of times the player has been warned
  // It can be increased from 1 to the maximum of allowed warnings
  // It does not represent the number of times the card has been received,
  // One Card object is created for each card received,
  // but it is only used to track how many yellow cards this is in the tournament and will be reset
  // to 1 if a new card is added and the old card has been placed to the highest possible turn. can
  private int times;

  private int suspendMilestone = 2;

  public YellowCard(String gameId) {
    this(Instant.now(), gameId);
  }

  public YellowCard(Instant time, String gameId) {
    super(time, gameId);
    this.times = 1;
  }

  public YellowCard(String gameId, int times) {
    this(Instant.now(), gameId, times);
  }

  public YellowCard(Instant time, String gameId, int times) {
    super(time, gameId);
    this.times = times;
  }

  public YellowCard(String gameId, int times, int suspendMilestone) {
    this(Instant.now(), gameId, times, suspendMilestone);
  }

  public YellowCard(Instant time, String gameId, int times, int suspendMilestone) {
    this(time, gameId, times);
    this.suspendMilestone = suspendMilestone;
  }

  @Override
  public boolean isPlayerSuspendNextGame() {
    return times >= suspendMilestone;
  }
}
