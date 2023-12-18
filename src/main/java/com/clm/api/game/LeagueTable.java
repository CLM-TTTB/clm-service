package com.clm.api.game;

import java.util.ArrayList;
import java.util.List;

/** LeagueTable */
@lombok.Getter
@lombok.Setter
public class LeagueTable {

  private String name;
  private List<String> teamIds;

  public LeagueTable() {
    this(null, new ArrayList<>());
  }

  public LeagueTable(int index) {
    this(index, new ArrayList<>());
  }

  public LeagueTable(int index, List<String> teamIds) {
    generateName(index);
    this.teamIds = teamIds;
  }

  public LeagueTable(String name, List<String> teamIds) {
    this.name = name;
    this.teamIds = teamIds;
  }

  public void generateName(int index) {
    this.name = String.valueOf(generateTableLetter(index));
  }

  private char generateTableLetter(int index) {
    return (char) ('A' + index);
  }
}
