package com.clm.api.utils;

/** Pair */
public class Pair<F, S> {

  private F first;
  private S second;

  public Pair(F first, S second) {
    this.first = first;
    this.second = second;
  }

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
  public void set(int index, Object value) {
    if (index == 0) {
      first = (F) value;
    } else if (index == 1) {
      second = (S) value;
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public boolean isEnough() {
    return first != null && second != null;
  }

  public boolean has(int index) {
    if (index == 0) {
      return hasFirst();
    } else if (index == 1) {
      return hasSecond();
    } else {
      throw new IndexOutOfBoundsException();
    }
  }

  public boolean hasFirst() {
    return first != null;
  }

  public F getFirst() {
    return first;
  }

  public void setFirst(F first) {
    this.first = first;
  }

  public boolean hasSecond() {
    return second != null;
  }

  public S getSecond() {
    return second;
  }

  public void setSecond(S second) {
    this.second = second;
  }

  public static <F, S> Pair<F, S> of(F first, S second) {
    return new Pair<>(first, second);
  }
}
