package com.clm.api.game;

import com.clm.api.interfaces.IIdTracker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** Round */
@lombok.Data
@lombok.AllArgsConstructor
public class Round {
  private List<Game> games;
  private List<Game> extraGames;

  public Round() {
    this(new ArrayList<>(), new ArrayList<>());
  }

  public Round(List<Game> games) {
    this.games = games;
    this.extraGames = new ArrayList<>();
  }

  public void addGame(Game game) {
    games.add(game);
  }

  public void addExtraGame(Game game) {
    extraGames.add(game);
  }

  public boolean isFinal() {
    return games.size() == 1;
  }

  public boolean isSemiFinal() {
    return games.size() == 2;
  }

  public boolean isQuarterFinal() {
    return games.size() == 4;
  }

  public boolean isEighthFinal() {
    return games.size() == 8;
  }

  public boolean isSixteenthFinal() {
    return games.size() == 16;
  }

  @JsonIgnore
  public Game getGame(int index) {
    return games.get(index);
  }

  public Game getExtraGame(int index) {
    return extraGames.get(index);
  }

  public boolean isFinished() {
    return games == null ? false : games.stream().allMatch(Game::hasWinner);
  }

  public List<IIdTracker<String>> getWinners() {
    if (games == null) return null;
    List<IIdTracker<String>> winnerGames =
        games.stream().filter(Game::hasWinner).map(Game::getWinner).collect(Collectors.toList());
    return winnerGames;
  }
}
