package com.clm.api.otp;

public interface OtpService {

  boolean send(String phoneNo);

  boolean verify(String phoneNo, String otp);
}
