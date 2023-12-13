package com.clm.api.interfaces;

import java.time.Instant;

/** ITimeTracker */
public interface IEventTracker {

  public Instant getTime();

  public String getGameId();
}
