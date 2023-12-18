// package com.clm.api.game;

// import com.clm.api.game.GameTracker.GameInfo;
// import com.clm.api.game.GameTracker.Round;
// import com.clm.api.game.GameTracker.TeamTracker;
// import com.clm.api.utils.Pair;
// import java.util.ArrayList;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.stream.Collectors;

// /** RoundFactory */
// public class RoundFactory {

//   public static GameTracker genNext(GameTracker tracker) {
//     if (tracker instanceof KnockOutGameTracker) {
//       return genNext((KnockOutGameTracker) tracker);
//     } else if (tracker instanceof RoundRobinGameTracker) {
//       return genNext((RoundRobinGameTracker) tracker);
//     } else if (tracker instanceof KnockOutWithRoundRobinGameTracker) {
//       return genNext((KnockOutWithRoundRobinGameTracker) tracker);
//     } else {
//       throw new UnsupportedOperationException("Competition type not supported");
//     }
//   }

//   // init the first round of the competition
//   private static GameTracker init(KnockOutGameTracker tracker) {
//     if (tracker.getRounds() == null) {
//       tracker.setRounds(new LinkedList<>());
//     }

//     List<List<LeagueTable>> tableCollections = tracker.getTableCollections();

//     if (tableCollections == null) {
//       throw new UnsupportedOperationException("Tables not set, please use tracker.setTables()");
//     }
//     int size = tableCollections.size();
//     if (size == 1) {
//       List<LeagueTable> tables = tableCollections.get(0);
//       int numberOfTables = tables.size();

//       Round round = new Round();
//       ArrayList<GameInfo> games = new ArrayList<>();

//       if (numberOfTables > 0) {
//         for (int i = 0; i < numberOfTables; i++) {
//           Pair<Integer, Integer> tableIndex = Pair.of(0, i);

//           List<String> teams = tables.get(i).getTeams();
//           int numberOfTeams = teams.size();

//           while (numberOfTeams > 1) {
//             TeamTracker[] teamTrackers =
//                 new TeamTracker[] {
//                   new TeamTracker(teams.get(--numberOfTeams), 0),
//                   new TeamTracker(teams.get(--numberOfTeams), 0)
//                 };
//             games.add(new GameInfo(tableIndex, teamTrackers));
//           }

//           if (numberOfTeams > 0) {
//             TeamTracker[] teamTrackers =
//                 new TeamTracker[] {new TeamTracker(teams.get(--numberOfTeams), 0), null};
//             games.add(new GameInfo(tableIndex, teamTrackers, null, 0));
//           }
//         }

//         round.setGames(games);
//         round.setName("Round" + tracker.getRounds().size() + 1);
//       } else {
//         throw new UnsupportedOperationException("Number of tables should be greater than 0");
//       }

//     } else {
//       throw new UnsupportedOperationException(
//           "Number of table collections in the first round should be 1, please use"
//               + " tracker.setTables()");
//     }

//     return tracker;
//   }

//   private static GameTracker init(RoundRobinGameTracker tracker) {
//     return tracker;
//   }

//   private static GameTracker init(KnockOutWithRoundRobinGameTracker tracker) {
//     return tracker;
//   }

//   private static GameTracker genNext(
//       KnockOutGameTracker tracker, boolean newTableCollection, int newTeamsPerTable) {
//     // no round has been played
//     if (tracker.getRounds() == null || tracker.getRounds().isEmpty()) {
//       init(tracker);
//     } else {
//       Round lastRound = tracker.getRounds().get(tracker.getRounds().size() - 1);
//       Round newRound = new Round();

//       if (newTableCollection) {
//         if (!lastRound.allGamesFinished()) {
//           throw new UnsupportedOperationException("All games in the last round should be
// finished");
//         }
//         LinkedList<TeamTracker> winners = lastRound.getWinners();
//         tracker
//             .getTableCollections()
//             .add(
//                 GameTrackerFactory.generateTables(
//                     winners.stream().map(TeamTracker::getId).collect(Collectors.toList()),
//                     newTeamsPerTable));

//       } else {

//         ArrayList<GameInfo> games = lastRound.getGames();
//         int numberOfGames = games.size();

//         if (numberOfGames < 2) {
//           throw new UnsupportedOperationException(
//               "Number of games in the last round should be greater than 1");
//         } else if (numberOfGames == 2) {
//           games.add(new GameInfo(Pair.of(0, 0), teamTrackers));
//         } else {
//           while (numberOfGames > 1) {
//             TeamTracker[] teamTrackers = new TeamTracker[2];
//             teamTrackers[0] = games.get(--numberOfGames).getWinners().get(0);
//             teamTrackers[1] = games.get(--numberOfGames).getWinners().get(0);
//             games.add(new GameInfo(Pair.of(0, 0), teamTrackers));
//           }
//         }

//         if (numberOfGames > 0) {
//           while (numberOfGames > 1) {
//             GameInfo game1 = games.get(--numberOfGames);

//             // TeamTracker[] teamTrackers =
//             //     new TeamTracker[] {
//             //       new TeamTracker(teams.get(--numberOfTeams), 0),
//             //       new TeamTracker(teams.get(--numberOfTeams), 0)
//             //     };
//             games.add(new GameInfo(tableIndex, teamTrackers));

//             Pair<Integer, Integer> tableIndex = Pair.of(0, i);
//             List<String> teams = tables.get(i).getTeams();
//             int numberOfTeams = teams.size();
//             while (numberOfTeams > 1) {
//               TeamTracker[] teamTrackers =
//                   new TeamTracker[] {
//                     new TeamTracker(teams.get(--numberOfTeams), 0),
//                     new TeamTracker(teams.get(--numberOfTeams), 0)
//                   };
//               games.add(new GameInfo(tableIndex, teamTrackers));
//             }
//             if (numberOfTeams > 0) {
//               TeamTracker[] teamTrackers =
//                   new TeamTracker[] {new TeamTracker(teams.get(--numberOfTeams), 0), null};
//               games.add(new GameInfo(tableIndex, teamTrackers, null, 0));
//             }
//           }
//         } else {
//           throw new UnsupportedOperationException(
//               "Number of games in the last round should be greater than 0");
//         }
//       }
//     }
//     return tracker;
//   }

//   private static GameTracker genNext(RoundRobinGameTracker tracker) {
//     return tracker;
//   }

//   private static GameTracker genNext(KnockOutWithRoundRobinGameTracker tracker) {
//     return tracker;
//   }
// }
