package com.clm.api.player;

import com.clm.api.interfaces.IEventTracker;
import java.time.Instant;
import org.springframework.data.annotation.Transient;

/** Goal */
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class GoalTracker implements IEventTracker {

  @Transient private static final long serialVersionUID = 1L;

  public static enum GoalType {
    HEADER,
    FREE_KICK,
    PENALTY,
    OWN_GOAL,
    CORNER,
    BACKHEEL,
    NORMAL
  }

  private Instant time;
  private String gameId;
  private GoalType type;

  public GoalTracker(Instant time, String gameId) {
    this.time = time;
    this.gameId = gameId;
    this.type = GoalType.NORMAL;
  }

  @Override
  public Instant getTime() {
    return time;
  }

  @Override
  public String getGameId() {
    return gameId;
  }
}
