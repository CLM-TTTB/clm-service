package com.clm.api.utils;

/** Pair */
public class DuplicatePair<T> extends Pair<T, T> {

  public DuplicatePair(T first, T second) {
    super(first, second);
  }

  @Override
  public T get(int index) {
    if (index == 0) return first;
    else if (index == 1) return second;
    else throw new IndexOutOfBoundsException();
  }
}
