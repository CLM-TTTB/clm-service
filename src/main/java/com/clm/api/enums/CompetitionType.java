package com.clm.api.enums;

import com.fasterxml.jackson.annotation.JsonTypeName;

/** CompetitionType */
@JsonTypeName
public enum CompetitionType {
  KNOCKOUT,
  ROUND_ROBIN,
  KNOCKOUT_WITH_ROUND_ROBIN;
}
