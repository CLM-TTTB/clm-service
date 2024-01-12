package com.clm.api.interfaces;

import com.clm.api.card.Card;
import java.util.List;

/** ICard */
public interface ICard {
  public int getNumberOfRedCards();

  public int getNumberOfYellowCards();

  public boolean isSuspended(String prevGameId);

  List<Card> getYellowCards();

  List<Card> getRedCards();
}
