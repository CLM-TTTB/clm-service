package com.clm.api.interfaces;

/** ICard */
public interface ICard {
  public int getNumberOfRedCards();

  public int getNumberOfYellowCards();

  public boolean isSuspended(String prevGameId);
}
