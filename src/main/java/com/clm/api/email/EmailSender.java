package com.clm.api.email;

import lombok.Getter;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

/** EmailSender */
@Getter
public class EmailSender {
  private String HOST;
  private int PORT;
  private String APP_EMAIL;
  private String APP_PASSWORD;

  private final Email email;

  public EmailSender() {
    email = new SimpleEmail();
  }

  public boolean send(String to, String subject, String body) {
    try {
      email.setHostName(System.getProperty("email.host"));
      email.setSmtpPort(Integer.valueOf(System.getProperty("email.port")));
      email.setAuthenticator(
          new DefaultAuthenticator(
              System.getProperty("email.username"), System.getProperty("email.password")));
      email.setStartTLSEnabled(true);

      email.setFrom(System.getProperty("email.username"));

      email.addTo(to);

      email.setSubject(subject);

      email.setMsg(body);

      email.send();
      return true;
    } catch (Exception e) {
      System.out.println("Error at SendingEmail.java: " + e);
      return false;
    }
  }
}
