package com.clm.api.constants;

/** Regex */
public class Regex {

  // password must have 1 upcase char and at least 8 character
  public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])[A-Za-z\\d@$!%*?&]{8,}$";
}
