package com.clm.api.enums;

/** AgeGroup */
public enum AgeGroup {
  U6(6),
  U12(12),
  U14(14),
  U16(16),
  U18(18),
  U21(21),
  U23(23),
  U25(25),
  U30(30),
  U35(35),
  U40(40),
  U45(45),
  ;

  byte maxAge;

  private AgeGroup(byte maxAge) {
    this.maxAge = maxAge;
  }

  private AgeGroup(int maxAge) {
    this((byte) maxAge);
  }

  public Byte getMaxAge() {
    return maxAge;
  }

  public String getAgeGroup() {
    return this.name();
  }

  public static AgeGroup getAgeGroup(int age) {
    for (AgeGroup ageGroup : AgeGroup.values()) {
      if (age <= ageGroup.getMaxAge()) {
        return ageGroup;
      }
    }
    return null;
  }

  public static AgeGroup getAgeGroup(String age) {
    return getAgeGroup(Integer.parseInt(age));
  }

  public static boolean isAgeInAgeGroup(int age, AgeGroup ageGroup) {
    return age <= ageGroup.getMaxAge();
  }
}
