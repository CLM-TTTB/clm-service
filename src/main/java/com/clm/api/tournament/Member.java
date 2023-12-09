package com.clm.api.tournament;

import com.clm.api.constants.message.ErrorMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Member */
@lombok.Data
@lombok.Builder
@Document(collection = "members")
// NOTE: The member of a team is not a user of the system.
public class Member {

  @Id private String id;

  @NotNull private String name;

  @Min(value = 6, message = ErrorMessage.AGE_INVALID)
  @NotNull
  private Byte age;

  private String image;

  private String description;

  private String currentTeamId;

  private List<String> previousTeamIds;

  // NOTE: If the member of a team has the account in the system, the userId will be set.
  private String userId;
}
