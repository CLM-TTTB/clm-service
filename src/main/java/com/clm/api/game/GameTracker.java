package com.clm.api.game;

import com.clm.api.enums.CompetitionType;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/** GameTracker */
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Document(collection = "game_trackers")
public class GameTracker {

  @lombok.Getter
  @lombok.Setter
  @lombok.NoArgsConstructor
  @lombok.AllArgsConstructor
  public class GameInfoTracker {
    private String name;
    private LinkedList<GameInfo> games;
  }

  @lombok.Getter
  @lombok.Setter
  @lombok.NoArgsConstructor
  @lombok.AllArgsConstructor
  public class Table {
    private String name;
    private List<String> teams;
  }

  @lombok.Getter
  @lombok.Setter
  @lombok.NoArgsConstructor
  @lombok.AllArgsConstructor
  public class TeamTracker {
    private String id;
    private String score;
  }

  @Data
  public class GameInfo {
    // tableId[0] is the id of corresponding table in roundRobinsTables or knockoutsTables
    // tableId[1] is the id exactly in the table
    // Example: tableId = [0, 1] means the game is played in the first table of roundRobinsTables
    private int[] tableId = new int[2];
    private TeamTracker[] teams = new TeamTracker[2];
    private String gameId;
    private Integer winnerId;

    public TeamTracker getWinner() {
      if (winnerId == null) {
        return null;
      }
      return teams[winnerId];
    }

    public TeamTracker getLoser() {
      if (winnerId == null) {
        return null;
      }
      return teams[1 - winnerId];
    }

    public void setWinner(TeamTracker team) {
      if (team == null) {
        return;
      }
      if (team.equals(teams[0])) {
        winnerId = 0;
      } else if (team.equals(teams[1])) {
        winnerId = 1;
      } else {
        throw new IllegalArgumentException("team must be one of the two teams");
      }
    }

    public void setWinnerId(Integer id) {
      if (id < 0 || id > 1) {
        throw new IllegalArgumentException("winnerId must be 0 or 1");
      }
      winnerId = id;
    }

    public void setWinnerId(String id) {
      if (id.equals(teams[0].getId())) {
        winnerId = 0;
      } else if (id.equals(teams[1].getId())) {
        winnerId = 1;
      } else {
        throw new IllegalArgumentException("winnerId must be 0 or 1");
      }
    }

    public TeamTracker getTeam1() {
      return teams[0];
    }

    public TeamTracker getTeam2() {
      return teams[1];
    }

    public void setTeam1(TeamTracker team) {
      teams[0] = team;
    }

    public void setTeam2(TeamTracker team) {
      teams[1] = team;
    }

    public String getTeam1Id() {
      return teams[0].id;
    }

    public String getTeam2Id() {
      return teams[1].id;
    }
  }

  private String tournamentId;
  private CompetitionType type;

  private LinkedList<GameInfoTracker> roundRobins;
  // roundRobinsTables is the list of table collections for each round robin
  // each round robin has a list of tables
  // they can use the same table collection for all round or
  // different table collection for each round
  // or same for some rounds and different for some rounds
  private List<List<Table>> roundRobinsTables;

  private LinkedList<GameInfoTracker> knockouts;

  // knockoutsTables is the list of table collections for each knockout
  // each knockout has a list of tables
  // they can use the same table collection for all knockout or
  // different table collection for each knockout
  // or same for some knockouts and different for some knockouts
  private List<List<Table>> knockoutsTables;
}
