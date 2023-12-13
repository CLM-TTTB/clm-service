package com.clm.api.team;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/** TeamTemplate */
@lombok.Getter
@lombok.Setter
@lombok.experimental.SuperBuilder
@Document(collection = "team_templates")
public class TeamTemplate {

  @Transient private static final long serialVersionUID = 1L;

  @Id protected String id;

  protected String creatorId;

  @NotNull protected String name;

  @Pattern(regexp = Regex.PHONE_NUMBER, message = ErrorMessage.PHONE_NUMBER_INVALID)
  @NotNull
  protected String phoneNo;

  @lombok.Builder.Default protected String image = "";
  @lombok.Builder.Default protected String description = "";

  // Save the list of uniform images url
  protected List<String> uniforms;

  protected List<TeamMember> members;
}
