package com.clm.api.otp;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** EmailController */
@RestController
@RequestMapping("/v1/auth/email")
@lombok.RequiredArgsConstructor
public class EmailController {

  private final TwoStepVerificationService twoStepVerificationService;

  @PostMapping("/send")
  public String send() {
    Map<String, Object> props = new HashMap<>();
    props.put("subject", "Hi");
    props.put("body", "123");
    twoStepVerificationService.send("sontungexpt@gmail.com", props);
    return "OK";
  }
}
