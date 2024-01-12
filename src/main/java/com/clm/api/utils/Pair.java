package com.clm.api.utils;

import com.clm.api.interfaces.IPair;

/** Pair */
public class Pair<F, S> implements IPair<F, S> {

  protected F first;
  protected S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public Pair(IPair<F, S> pair) {
    this.first = pair.getFirst();
    this.second = pair.getSecond();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object get(int index) {
    if (index == 0) {
      return first;
    } else if (index == 1) {
      return second;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void set(int index, Object value) {
    if (index == 0) {
      first = (F) value;
    } else if (index == 1) {
      second = (S) value;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public boolean isEnough() {
    return first != null && second != null;
  }

  @Override
  public boolean has(int index) {
    if (index == 0) {
      return hasFirst();
    } else if (index == 1) {
      return hasSecond();
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  @Override
  public boolean hasFirst() {
    return first != null;
  }

  @Override
  public F getFirst() {
    return first;
  }

  @Override
  public void setFirst(F first) {
    this.first = first;
  }

  @Override
  public boolean hasSecond() {
    return this.second != null;
  }

  @Override
  public S getSecond() {
    return this.second;
  }

  @Override
  public void setSecond(S second) {
    this.second = second;
  }

  public static <F, S> Pair<F, S> of(F first, S second) {
    return new Pair<>(first, second);
  }
}
