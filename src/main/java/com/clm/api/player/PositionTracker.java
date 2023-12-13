package com.clm.api.player;

import com.clm.api.interfaces.IEventTracker;
import java.time.Instant;
import org.springframework.data.annotation.Transient;

/** PlayerPosition */
@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
public class PositionTracker implements IEventTracker {

  @Transient private static final long serialVersionUID = 1L;

  public static enum Position {
    GOALKEEPER,
    DEFENDER,
    MIDFIELDER,
    ATTACKER,
    SKIPPER, // Captain
    SUBSTITUTE, // Reserves
    ;
  }

  private Position position;
  private Instant time;
  private Instant timeOff;
  private String gameId;

  @Override
  public Instant getTime() {
    return time;
  }

  @Override
  public String getGameId() {
    return gameId;
  }
}
