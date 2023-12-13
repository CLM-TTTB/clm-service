package com.clm.api.card;

import com.clm.api.interfaces.IEventTracker;
import java.time.Instant;
import org.springframework.data.annotation.Transient;

/** Card */
@lombok.Getter
@lombok.Setter
public abstract class Card implements IEventTracker {

  @Transient private static final long serialVersionUID = 1L;

  protected Instant time;

  // the id of the game where the card was given
  protected String gameId;

  @Override
  public Instant getTime() {
    return time;
  }

  @Override
  public String getGameId() {
    return gameId;
  }

  public Card(String gameId) {
    this(Instant.now(), gameId);
  }

  public Card(Instant time, String gameId) {
    this.time = time;
    this.gameId = gameId;
  }

  public abstract boolean isPlayerSuspendNextGame();

  public boolean sameGame(Card card) {
    return card.getGameId().equals(this.gameId);
  }

  public boolean fromGame(String gameId) {
    return gameId.equals(this.gameId);
  }
}
