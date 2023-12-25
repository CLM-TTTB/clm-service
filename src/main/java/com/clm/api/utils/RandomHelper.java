package com.clm.api.utils;

/** RandomHelper */
public class RandomHelper {

  public static char randomChar() {
    return Math.random() > 0.5 ? randomLowercaseChar() : randomUppercaseChar();
  }

  public static char randomLowercaseChar() {
    return (char) (Math.random() * 26 + 'a');
  }

  public static char randomUppercaseChar() {
    return (char) (Math.random() * 26 + 'A');
  }

  public static int randomNum() {
    return (int) (Math.random() * 10);
  }

  public static char randomNumOrChar() {
    return Math.random() > 0.5 ? randomChar() : (char) randomNum();
  }

  public static String randomNumOrChar(int length) {
    String str = "";
    for (int i = 0; i < length; i++) {
      str += randomNumOrChar();
    }
    return str;
  }
}
