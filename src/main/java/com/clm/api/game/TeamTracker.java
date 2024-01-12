package com.clm.api.game;

import com.clm.api.interfaces.IIdTracker;
import com.clm.api.interfaces.IRankObserver;
import org.springframework.data.annotation.Id;

@lombok.Getter
@lombok.Setter
@lombok.experimental.SuperBuilder
public abstract class TeamTracker implements IIdTracker<String>, IRankObserver {
  @Id protected String id;
  protected String name;

  // default is max rank (1)
  // rank will be 1 > 2 > 3 > 4 > 5 > 6 > 7 > 8 > 9 > 10
  protected Integer rank = 1;
  protected Integer goalDifference = 0;

  protected String uniform = "";
  protected Integer goalsFor = null;

  protected TeamTracker(String id, String name) {
    this.id = id;
    this.name = name;
  }

  protected TeamTracker() {}

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  public abstract void win();

  public abstract void lose();

  public abstract void draw();

  public abstract void win(TeamTracker loser, int goalsFor, int goalsAgainst);

  public abstract void lose(TeamTracker winner, int goalsFor, int goalsAgainst);

  public abstract void draw(TeamTracker opponent, int goalsFor, int goalsAgainst);
}
