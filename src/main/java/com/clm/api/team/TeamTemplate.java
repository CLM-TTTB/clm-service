package com.clm.api.team;

import com.clm.api.constants.Regex;
import com.clm.api.constants.message.ErrorMessage;
import com.clm.api.team.member.TeamMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/** TeamTemplate */
@lombok.Getter
@lombok.Setter
@lombok.Builder
@lombok.AllArgsConstructor
@Document(collection = "team_templates")
public class TeamTemplate {

  @Transient private static final long serialVersionUID = 1L;

  @Id private String id;

  private String creatorId;

  @NotBlank private String name;

  @NotBlank private String teamName;

  @Pattern(regexp = Regex.PHONE_NUMBER, message = ErrorMessage.PHONE_NUMBER_INVALID)
  @NotBlank
  private String phoneNo;

  @lombok.Builder.Default private String image = "";
  @lombok.Builder.Default private String description = "";

  @lombok.Builder.Default private List<TeamMember> members = new ArrayList<>();

  // Save the list of uniform images url
  @lombok.Builder.Default private List<String> uniforms = new ArrayList<>();

  public TeamTemplate() {
    this.image = "";
    this.description = "";
    this.members = new ArrayList<>();
    this.uniforms = new ArrayList<>();
  }

  public TeamTemplate(Team team) {
    this.creatorId = team.getCreatorId();
    this.teamName = team.getName();
    this.phoneNo = team.getPhoneNo();
    this.image = team.getImage();
    this.description = team.getDescription();
    this.members = team.getMembers();
    this.uniforms = team.getUniforms();
  }

  public static TeamTemplate from(Team team) {
    return new TeamTemplate(team);
  }
}
