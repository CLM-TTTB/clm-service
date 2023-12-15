package com.clm.api.team.member.player;

import com.clm.api.team.member.TeamMember;
import java.util.List;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/** Player */
@lombok.Getter
@lombok.Setter
@lombok.experimental.SuperBuilder
@Document(collection = "players")
public class Player extends TeamMember {

  @Transient private static final long serialVersionUID = 1L;

  public Player() {
    super();
  }

  public Player(String id, String name, Byte age) {
    super(id, name, age);
  }

  public Player(
      String id,
      String name,
      Byte age,
      String image,
      String description,
      String currentTeamId,
      List<String> previousTeamIds) {
    super(id, name, age, image, description, currentTeamId, previousTeamIds);
  }

  public Player(
      String id,
      String name,
      Byte age,
      String image,
      String description,
      String currentTeamId,
      List<String> previousTeamIds,
      String userId) {
    super(id, name, age, image, description, currentTeamId, previousTeamIds, userId);
  }
}
