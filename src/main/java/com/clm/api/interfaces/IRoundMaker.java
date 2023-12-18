package com.clm.api.interfaces;

import com.clm.api.game.Round;
import java.util.List;

/** IRoundManager */
public interface IRoundMaker {

  List<Round> updateAllRounds();

  List<Round> createAllRounds();
}
