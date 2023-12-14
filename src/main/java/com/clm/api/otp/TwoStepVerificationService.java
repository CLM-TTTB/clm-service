package com.clm.api.otp;

import java.util.Map;

public interface TwoStepVerificationService {

  public boolean send(String identifier, Map<String, Object> props);

  public boolean verify(String identifier, Map<String, Object> props);
}
