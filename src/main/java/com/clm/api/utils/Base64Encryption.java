package com.clm.api.utils;

import java.util.Base64;

/** Base64Encryption */
public class Base64Encryption {
  /**
   * Encodes a string using Base64 encoding.
   *
   * @param str The input string to be encoded.
   * @return The Base64 encoded string.
   */
  public static String encode(String str) {
    return new String(Base64.getEncoder().encode(str.getBytes()));
  }

  /**
   * Decodes a Base64 encoded string.
   *
   * @param str The Base64 encoded string to be decoded.
   * @return The decoded string.
   */
  public static String decode(String str) {
    return new String(Base64.getDecoder().decode(str.getBytes()));
  }

  /**
   * Encodes a string with additional modifications. The random SecretKey is placed in the middle of
   * the string, and the last three characters represent the index, length of the random string, and
   * the number of characters of the insertIndex value.
   *
   * @param str The input string to be encoded.
   * @return The modified and encoded string.
   */
  public static String encodeBetter(String str) {
    String secretKeyRandom = randomNumOrChar((int) (Math.random() * 9));
    int secretKeyLength = secretKeyRandom.length();

    String encodedString = encode(str);

    int insertIndex = 0;
    int insertIndexLength = 0;

    do {
      insertIndex = (int) (Math.random() * encodedString.length() - 1);
      insertIndexLength = String.valueOf(insertIndex).length();
    } while (insertIndexLength > 9);

    encodedString =
        insertString(encodedString, insertIndex, secretKeyRandom)
            + insertIndex
            + secretKeyLength
            + insertIndexLength;

    return encodedString;
  }

  /**
   * Decodes a string that was encoded with additional modifications.
   *
   * @param str The modified and encoded string to be decoded.
   * @return The decoded string.
   */
  public static String decodeBetter(String str) {
    int length = str.length();
    int insertIndexLength = Integer.parseInt(str.substring(length - 1));
    int secretKeyLength = Integer.parseInt(str.substring(length - 1 - 1, length - 1));
    int insertIndex =
        Integer.parseInt(str.substring(length - 1 - 1 - insertIndexLength, length - 1 - 1));

    String encodedString =
        str.substring(0, insertIndex)
            + str.substring(insertIndex + secretKeyLength, length - 1 - 1 - insertIndexLength);

    return decode(encodedString);
  }

  // Inserts a string into another string at a specific index
  private static String insertString(String original, int index, String insertStr) {
    return original.substring(0, index) + insertStr + original.substring(index);
  }

  // random char both uppercase and lowercase
  private static char randomChar() {
    return Math.random() > 0.5 ? randomLowercaseChar() : randomUppercaseChar();
  }

  private static char randomLowercaseChar() {
    return (char) (Math.random() * 26 + 'a');
  }

  private static char randomUppercaseChar() {
    return (char) (Math.random() * 26 + 'A');
  }

  private static int randomNum() {
    return (int) (Math.random() * 10);
  }

  private static char randomNumOrChar() {
    return Math.random() > 0.5 ? randomChar() : (char) randomNum();
  }

  private static String randomNumOrChar(int length) {
    String str = "";
    for (int i = 0; i < length; i++) {
      str += randomNumOrChar();
    }
    return str;
  }
}
