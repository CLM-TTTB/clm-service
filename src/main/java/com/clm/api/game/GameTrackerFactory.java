// package com.clm.api.game;

// import com.clm.api.enums.CompetitionType;
// import java.util.List;

// /** GameTrackerFactory */
// public class GameTrackerFactory {

//   public static GameTracker create(
//       String tournamentId, CompetitionType type, List<String> teamIds) {
//     return create(tournamentId, type, teamIds, 4);
//   }

//   public static GameTracker create(
//       String tournamentId, CompetitionType type, List<String> teamIds, int teamsPerTable) {
//     switch (type) {
//       case KNOCKOUT:
//       case ROUND_ROBIN:
//       case ROUND_ROBIN_WITH_KNOCKOUT:
//       default:
//         throw new UnsupportedOperationException("Competition type not supported");
//     }
//   }

//   private GameTracker createKnockout(String tournamentId, List<String> teamIds, int
// teamsPerTable) {
//     KnockOutGameTracker tracker = new KnockOutGameTracker(tournamentId);
//     // tracker.getTableCollections().add(generateTables(teamIds, teamsPerTable));

//     return tracker;
//   }

//   private GameTracker createRoundRobin(
//       String tournamentId, List<String> teamIds, int teamsPerTable) {
//     RoundRobinGameTracker tracker = new RoundRobinGameTracker(tournamentId);
//     // tracker.getTables().add(generateTables(teamIds, teamsPerTable));

//     return tracker;
//   }
// }
