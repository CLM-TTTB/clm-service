package com.clm.api.user;

/** Swapper */
public class Swapper {
  public static <T> void swap(T a, T b) {
    T temp = a;
    a = b;
    b = temp;
  }
}
