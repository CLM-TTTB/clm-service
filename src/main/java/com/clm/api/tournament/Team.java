package com.clm.api.tournament;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Team */
@lombok.Getter
@lombok.Setter
@lombok.Builder
@Document(collection = "teams")
public class Team {

  @Id private String id;

  private String creatorId;

  @NotNull private String name;

  @Pattern(regexp = Regex.PHONE_NUMBER, message = ErrorMessage.PHONE_NUMBER_INVALID)
  @NotNull
  private String phoneNo;

  @lombok.Builder.Default private String image = "";
  @lombok.Builder.Default private String description = "";

  // Save the list of uniform images url
  private List<String> uniforms;

  private List<Member> members;
}
