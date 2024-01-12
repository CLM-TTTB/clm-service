package com.clm.api.interfaces;

import java.time.Instant;

/** IRank */
public interface IRank {

  void attach(IRankObserver observer);

  void detach(IRankObserver observer);

  void notifyObservers();

  Instant getRankingTime();
}
