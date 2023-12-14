package com.clm.api.otp;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** EmailOtpService */
@Service
@lombok.RequiredArgsConstructor
public class EmailOtpService implements TwoStepVerificationService {
  private static final long serialVersionUID = 1L;

  @Value("${email.host}")
  private String HOST;

  @Value("${email.port}")
  private int PORT;

  @Value("${email.username}")
  private String APP_EMAIL;

  @Value("${email.password}")
  private String APP_PASSWORD;

  @PostConstruct
  public void init() {}

  @Override
  public boolean send(String identifier, Map<String, Object> props) {
    try {
      Email email = new SimpleEmail();

      email.setHostName(HOST);
      email.setSmtpPort(PORT);
      email.setAuthenticator(new DefaultAuthenticator(APP_EMAIL, APP_PASSWORD));
      email.setStartTLSEnabled(true);

      email.setFrom(APP_EMAIL);

      email.addTo(identifier);

      email.setSubject(props.get("subject").toString());
      email.setMsg(props.get("body").toString());

      email.send();
      return true;
    } catch (Exception e) {
      System.out.println("Error at SendingEmail.java: " + e);
      return false;
    }
  }

  @Override
  public boolean verify(String identifier, Map<String, Object> props) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'verify'");
  }
}
