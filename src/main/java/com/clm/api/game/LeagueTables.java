package com.clm.api.game;

import com.clm.api.interfaces.IIdTracker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** LeagueTables */
public class LeagueTables extends ArrayList<LeagueTable> {

  private static final long serialVersionUID = 1L;

  public LeagueTables() {
    super();
  }

  public LeagueTables(int initialCapacity) {
    super(initialCapacity);
  }

  public LeagueTables(List<LeagueTable> tables) {
    super(tables);
  }

  public LeagueTables(LeagueTables tables) {
    super(tables);
  }

  public LeagueTables(LeagueTable table) {
    super();
    add(table);
  }

  public List<String> getTeamIds() {
    return this.stream().map(LeagueTable::getTeamIds).flatMap(List::stream).toList();
  }

  public static LeagueTables fromIIdTrackerList(
      List<IIdTracker<String>> teamIds, int teamsPerTable) {
    List<String> ids = teamIds.stream().map(IIdTracker::getId).toList();
    return from(ids, teamsPerTable);
  }

  /**
   * Creates league tables from a list of team ids and the number of teams per table.
   *
   * <p>Teams are distributed evenly across tables. If the number of teams is not divisible by the
   * number of teams per table, the last table will have less teams than the others.
   *
   * <p>Example:
   *
   * <p>Teams: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] Teams per table: 4
   *
   * <p>Tables: { 0: [0, 1, 2, 3] 1: [4, 5, 6, 7] 2: [8, 9] }
   *
   * @param teamIds list of team ids
   * @param teamsPerTable number of teams per table
   * @return league tables
   */
  public static LeagueTables from(List<String> teamIds, int teamsPerTable) {
    int size = teamIds.size();

    if (size < 2) {
      throw new IllegalArgumentException("At least 2 teams are required");
    }
    if (teamsPerTable < 2) {
      throw new IllegalArgumentException("At least 2 teams per table are required");
    }
    if (teamsPerTable > size) {
      throw new IllegalArgumentException("Teams per table cannot be greater than number of teams");
    }

    if (teamsPerTable == size) {
      return new LeagueTables(new LeagueTable(0, new ArrayList<>(teamIds)));
    }

    // min teams per table = 50 % of teams per table
    // e.g. 4 teams per table -> 3 teams per table
    // e.g. 5 teams per table -> 3 teams per table
    int minTeamsPerTable = teamsPerTable / 2 + 1;

    // Draw lots to determine which team will play in which table
    List<String> shuffledTeamIds = new ArrayList<>(teamIds);
    Collections.shuffle(shuffledTeamIds);

    LeagueTables tables = new LeagueTables();
    int tableIndex = 0;

    while (size >= minTeamsPerTable) {
      int subListSize = Math.min(size, teamsPerTable);
      tables.add(
          new LeagueTable(
              tableIndex++, new ArrayList<>(shuffledTeamIds.subList(size - subListSize, size))));
      size -= subListSize;
    }

    int numberOfTables = tables.size();
    while (size > 0) {
      if (tableIndex == numberOfTables) {
        tableIndex = 0;
      }
      tables.get(tableIndex++).getTeamIds().add(shuffledTeamIds.get(--size));
    }

    return tables;
  }
}
