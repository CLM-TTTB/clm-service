package com.clm.api.enums;

import com.fasterxml.jackson.annotation.JsonTypeName;

/** CompetitionType */
@JsonTypeName
public enum CompetitionType {
  KNOCKOUT,
  ROUND_ROBIN,
  ROUND_ROBIN_WITH_KNOCKOUT;
}
