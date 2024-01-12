package com.clm.api.interfaces;

import java.time.Instant;
import java.util.List;

/** IRank */
public interface IRank {

  void attach(IRankObserver observer);

  void attach(List<? extends IRankObserver> observers);

  void detach(IRankObserver observer);

  void detachAll();

  void notifyObservers();

  Instant getRankingTime();
}
