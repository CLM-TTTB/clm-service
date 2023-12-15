package com.clm.api.auth;

import java.util.Map;

public interface TwoStepVerificationService {

  public boolean send(String identifier, Map<String, Object> props);

  public boolean verify(String identifier, Map<String, Object> props);
}
