package com.clm.api.card;

import java.time.Instant;

/** RedCard */
@lombok.Getter
@lombok.Setter
public class RedCard extends Card {

  public RedCard(Instant time, String gameId) {
    super(time, gameId);
  }

  public RedCard(String gameId) {
    super(gameId);
  }

  @Override
  public boolean isPlayerSuspendNextGame() {
    return true;
  }
}
