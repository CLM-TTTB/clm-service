package com.clm.api.otp;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** EmailController */
@RestController
@RequestMapping("/v1/auth/email")
@lombok.RequiredArgsConstructor
public class EmailController {

  private final OtpService otpService;

  @PostMapping("/send")
  public String send() {
    otpService.send("sontungexpt@gmail.com");
    return "OK";
  }
}
