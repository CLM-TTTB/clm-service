package com.clm.api.card;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

/** CardFactory */
public class CardFactory {
  private static final long serialVersionUID = 1L;

  public static enum CardType {
    // This card is used to track the number of yellow cards a player has received
    // If enough yellow cards are received in last 5 games, the player is suspended
    INHERITANCE,
    // This card is used to track the number of yellow cards a player has received
    // If enough yellow cards are received in 1 unique game, the player is suspended
    ONE_GAME,
  }

  public static enum CardColor {
    YELLOW(Set.of(CardType.INHERITANCE, CardType.ONE_GAME)),
    RED(Collections.emptySet()),
    ;

    @lombok.Getter private final Set<CardType> types;

    private CardColor(Set<CardType> types) {
      this.types = types;
    }
  }

  public static Card createYellowCard(
      CardType type, Instant time, String gameId, Integer suspendMilestone, Card previousCard) {
    switch (type) {
      case INHERITANCE:
        {
          YellowCard yellowCard = new YellowCard(time, gameId);
          if (suspendMilestone == null) {
            suspendMilestone = yellowCard.getSuspendMilestone();
          } else {
            yellowCard.setSuspendMilestone(suspendMilestone);
          }

          if (previousCard != null) {
            yellowCard.setTimes(((YellowCard) previousCard).getTimes() + 1);
            if (yellowCard.getTimes() > suspendMilestone) {
              yellowCard.setTimes(1);
            }
          }
          return yellowCard;
        }
      case ONE_GAME:
        {
          YellowCard yellowCard = new YellowCard(time, gameId);
          // if get other yellow card in one game, then the player is suspended
          if (previousCard != null && previousCard.fromGame(gameId)) {
            yellowCard.setTimes(((YellowCard) previousCard).getTimes() + 1);
            if (yellowCard.getTimes() > suspendMilestone) {
              yellowCard.setTimes(1);
            }
          }
          return yellowCard;
        }
      default:
        throw new IllegalArgumentException("Invalid card type");
    }
  }

  public static Card createYellowCard(
      CardType type, Instant time, String gameId, Card previousCard) {
    return createYellowCard(type, time, gameId, null, previousCard);
  }

  public static Card createYellowCard(CardType type, String gameId, Card previousCard) {
    return createYellowCard(type, Instant.now(), gameId, null, previousCard);
  }

  public static Card createYellowCard(
      CardType type, String gameId, Integer suspendMilestone, Card previousCard) {
    return createYellowCard(type, Instant.now(), gameId, suspendMilestone, previousCard);
  }

  public static Card createYellowCard(String gameId, Integer suspendMilestone, Card previousCard) {
    return createYellowCard(
        CardType.ONE_GAME, Instant.now(), gameId, suspendMilestone, previousCard);
  }

  public static Card createYellowCard(String gameId, Card previousCard) {
    return createYellowCard(CardType.ONE_GAME, Instant.now(), gameId, null, previousCard);
  }

  public static Card createRedCard(CardType type, Instant time, String gameId) {
    return new RedCard(time, gameId);
  }

  public static Card createRedCard(Instant time, String gameId) {
    return createRedCard(CardType.ONE_GAME, time, gameId);
  }

  public static Card createCard(
      CardColor color, CardType type, Instant time, String gameId, Card previousCard) {
    return createCard(color, type, time, gameId, null, previousCard);
  }

  public static Card createCard(CardColor color, CardType type, String gameId, Card previousCard) {
    return createCard(color, type, Instant.now(), gameId, previousCard);
  }

  public static Card createCard(CardColor color, Instant time, String gameId, Card previousCard) {
    return createCard(color, CardType.ONE_GAME, time, gameId, previousCard);
  }

  public static Card createCard(CardColor color, String gameId, Card previousCard) {
    return createCard(color, Instant.now(), gameId, previousCard);
  }

  public static Card createCard(
      CardColor color,
      CardType type,
      Instant time,
      String gameId,
      Integer suspendMilestone,
      Card previousCard) {
    switch (color) {
      case YELLOW:
        return createYellowCard(type, time, gameId, suspendMilestone, previousCard);
      case RED:
        return createRedCard(time, gameId);
      default:
        throw new IllegalArgumentException("Invalid card type");
    }
  }

  public static Card createCard(
      CardColor color, CardType type, String gameId, Integer suspendMilestone, Card previousCard) {
    return createCard(color, type, Instant.now(), gameId, suspendMilestone, previousCard);
  }

  public static Card createCard(
      CardColor color, Instant time, String gameId, Integer suspendMilestone, Card previousCard) {
    return createCard(color, CardType.ONE_GAME, time, gameId, suspendMilestone, previousCard);
  }

  public static Card createCard(
      CardColor color, String gameId, Integer suspendMilestone, Card previousCard) {
    return createCard(
        color, CardType.ONE_GAME, Instant.now(), gameId, suspendMilestone, previousCard);
  }
}
