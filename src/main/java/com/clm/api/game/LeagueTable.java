package com.clm.api.game;

import java.util.ArrayList;
import java.util.List;

/** LeagueTable */
@lombok.Getter
@lombok.Setter
public class LeagueTable<T> {

  private String name;
  private List<T> teams;

  public LeagueTable() {
    this(null, new ArrayList<>());
  }

  public LeagueTable(int index) {
    this(index, new ArrayList<>());
  }

  public LeagueTable(int index, List<T> teamIds) {
    generateName(index);
    this.teams = teamIds;
  }

  public LeagueTable(String name, List<T> teamIds) {
    this.name = name;
    this.teams = teamIds;
  }

  public void generateName(int index) {
    this.name = String.valueOf(generateTableLetter(index));
  }

  private char generateTableLetter(int index) {
    return (char) ('A' + index);
  }
}
