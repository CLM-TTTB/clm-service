package com.clm.api.otp;

public interface OtpService {

  boolean send(String identifier);

  boolean verify(String phoneNo, String otp);
}