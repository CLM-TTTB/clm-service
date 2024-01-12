package com.clm.api.team.member.player;

import com.clm.api.card.Card;
import com.clm.api.card.CardFactory;
import com.clm.api.card.RedCard;
import com.clm.api.card.YellowCard;
import com.clm.api.game.Game;
import com.clm.api.interfaces.ICard;
import java.time.Instant;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Transient;

/** TrackedPlayer */
@lombok.experimental.SuperBuilder
@lombok.Getter
@lombok.Setter
public class TrackedPlayer extends Player implements ICard {

  @Transient private static final long serialVersionUID = 1L;

  private LinkedList<GoalTracker> goals;
  private LinkedList<PositionTracker> positions;

  @lombok.Builder.Default private LinkedList<Card> yellowCards = new LinkedList<>();

  @Override
  public List<Card> getYellowCards() {
    return yellowCards;
  }

  @lombok.Builder.Default private LinkedList<Card> redCards = new LinkedList<>();

  @Override
  public List<Card> getRedCards() {
    return redCards;
  }

  @Override
  public int getNumberOfRedCards() {
    return redCards == null ? 0 : redCards.size();
  }

  @Override
  public int getNumberOfYellowCards() {
    return yellowCards == null ? 0 : yellowCards.size();
  }

  public void addYellowCard(Instant time, String gameId) {
    yellowCards.add(
        CardFactory.createYellowCard(CardFactory.CardType.ONE_GAME, gameId, yellowCards.getLast()));
  }

  public void addRedCard(Instant time, String gameId) {
    redCards.add(CardFactory.createRedCard(gameId));
  }

  public TrackedPlayer() {
    super();
    this.yellowCards = new LinkedList<>();
    this.redCards = new LinkedList<>();
    this.id = UUID.randomUUID().toString();
  }

  public TrackedPlayer(TrackedPlayer player) {
    super(player);
    this.yellowCards = new LinkedList<>();
    this.redCards = new LinkedList<>();
    this.id = UUID.randomUUID().toString();
  }

  public TrackedPlayer(Player player) {
    super(player);
    this.yellowCards = new LinkedList<>();
    this.redCards = new LinkedList<>();
    this.id = UUID.randomUUID().toString();
  }

  /**
   * Checks if the player is suspended for the this game
   *
   * <p>If the player has 2 yellow cards in the last 5 games, he is suspended
   *
   * <p>If the player has 1 red card in the last game, he is suspended
   *
   * @param prevGameId
   * @return true if the player is suspended
   */
  @Override
  public boolean isSuspended(String prevGameId) {
    if (hasPlayedAtLeastOneGame()) {
      if (getNumberOfRedCards() > 0) {
        RedCard card = (RedCard) redCards.getLast();
        return card.fromGame(prevGameId) && card.isPlayerSuspendNextGame();
      }

      if (getNumberOfYellowCards() > 1) {
        YellowCard card = (YellowCard) yellowCards.getLast();
        return card.fromGame(prevGameId) && card.isPlayerSuspendNextGame();
      }
    }
    return false;
  }

  public boolean isSuspended(Game game) {
    return isSuspended(game.getPreviousGameId());
  }

  public List<String> getPlayedGameIds() {
    Set<String> gameIds = new HashSet<>();
    if (hasPlayedAtLeastOneGame()) {
      positions.stream().forEach(position -> gameIds.add(position.getGameId()));
    }
    return new LinkedList<>(gameIds);
  }

  public boolean hasPlayedInGame(String gameId) {
    if (hasPlayedAtLeastOneGame()
        && positions.stream().anyMatch(position -> position.getGameId().equals(gameId))) {
      return true;
    }
    return false;
  }

  public boolean hasPlayedAtLeastOneGame() {
    return positions != null && !positions.isEmpty();
  }

  public String getLatestPlayedGameId() {
    if (hasPlayedAtLeastOneGame()) {
      return positions.getLast().getGameId();
    }
    return null;
  }
}
