// package com.clm.api.game;

// import com.clm.api.enums.CompetitionType;
// import com.clm.api.game.Round.GameInfo;
// import com.clm.api.game.Round.TeamTracker;
// import com.clm.api.interfaces.IRoundMaker;
// import com.clm.api.utils.Pair;
// import java.util.ArrayList;
// import java.util.List;

// /** RoundRobinGameTracker */
// @lombok.Getter
// @lombok.Setter
// @lombok.experimental.SuperBuilder
// public class RoundRobinGameTracker extends GameTracker implements IRoundMaker {

//   private Round round;

//   public RoundRobinGameTracker(String tournamentId, List<String> teamIds) {
//     super(tournamentId, teamIds, CompetitionType.ROUND_ROBIN);
//   }

//   @Override
//   public void initFirstRound() {
//     for (int i = 0; i < teamIds.size(); i++) {
//       for (int j = i + 1; j < teamIds.size(); j++) {
//         List<GameInfo> games = new ArrayList<>();
//         Pair<TeamTracker, TeamTracker> teams =
//             Pair.of(new TeamTracker(teamIds.get(0)), new TeamTracker(teamIds.get(1)));
//         games.add(new GameInfo(teams));
//         rounds.add(new Round(games));
//       }
//     }
//   }

//   @Override
//   public void createNextRound() {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'createNextRound'");
//   }

//   @Override
//   public void updateRound(int roundIndex) {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'updateRound'");
//   }

//   @Override
//   public void createAllRounds() {
//     // TODO Auto-generated method stub
//     throw new UnsupportedOperationException("Unimplemented method 'createAllRounds'");
//   }
// }
