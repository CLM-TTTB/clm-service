package com.clm.api.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** IPatchSubject */
public interface IPatchSubject {

  @JsonIgnore
  String[] getIgnoredFields();
}
