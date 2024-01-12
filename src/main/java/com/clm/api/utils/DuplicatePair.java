package com.clm.api.utils;

/** Pair */
public class DuplicatePair<T> extends Pair<T, T> {

  public DuplicatePair(T first, T second) {
    super(first, second);
  }

  @Override
  public T get(int index) {
    if (index == 0) return this.first;
    else if (index == 1) return this.second;
    else throw new IndexOutOfBoundsException();
  }
}
