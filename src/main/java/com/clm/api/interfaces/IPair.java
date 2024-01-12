package com.clm.api.interfaces;

import com.clm.api.utils.Pair;

/** IPair */
public interface IPair<F, S> {

  F getFirst();

  S getSecond();

  void setFirst(F first);

  void setSecond(S second);

  boolean isEnough();

  boolean isEmpty();

  boolean hasFirst();

  boolean hasSecond();

  <D> D get(int index);

  <D> void set(int index, D value);

  boolean has(int index);

  static <F, S> IPair<F, S> of(F first, S second) {
    return new Pair<>(first, second);
  }
}
