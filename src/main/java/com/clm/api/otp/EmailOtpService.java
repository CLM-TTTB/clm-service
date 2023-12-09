package com.clm.api.otp;

import org.springframework.stereotype.Service;

/** EmailOtpService */
@Service
public class EmailOtpService implements OtpService {
  private static final long serialVersionUID = 1L;

  // @Value("${email.host}")
  // private String HOST;

  // @Value("${email.tsl-port}")
  // private int TSL_PORT;

  // @Value("${email.account}")
  // private String APP_EMAIL;

  // @Value("${email.password}")
  // private String APP_PASSWORD;

  @Override
  public boolean send(String identifier) {
    throw new UnsupportedOperationException("Unimplemented method 'send'");
    // Properties props = new Properties();
    // props.put("mail.smtp.auth", "true");
    // props.put("mail.smtp.host", HOST);
    // props.put("mail.smtp.starttls.enable", "true");
    // props.put("mail.smtp.port", TSL_PORT);
    // // props.put("mail.smtp.ssl.trust", HOST);
    // props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    // props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    // Session session =
    //     Session.getInstance(
    //         props,
    //         new javax.mail.Authenticator() {
    //           protected PasswordAuthentication getPasswordAuthentication() {
    //             return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
    //           }
    //         });

    // try {
    //   System.out.println("Sending Verification Link to " + identifier);
    //   MimeMessage message = new MimeMessage(session);
    //   message.setFrom(new InternetAddress(APP_EMAIL));
    //   message.addRecipient(Message.RecipientType.TO, new
    // InternetAddress("sontungexpt@gmail.com"));
    //   message.setSubject("Email Verification Link");
    //   message.setSubject("Testing Subject");
    //   message.setText("Welcome to clm.com");

    //   // message.setText("Welcome to clm.com");
    //   // message.setText(

    //   //     "Click this link to confirm your email address and complete setup for your account."
    //   //         + "\n\nVerification Link: "
    //   //         + "http://localhost:8080/EmailVerification/ActivateAccount?key1="
    //   //         + userEmail
    //   //         + "&key2="
    //   //         + myHash);

    //   Transport.send(message);
    //   System.out.println("Successfully sent Verification Link");
    //   return true;
    // } catch (Exception e) {
    //   System.out.println("Error at SendingEmail.java: " + e);
    //   return false;
    // }
  }

  @Override
  public boolean verify(String phoneNo, String otp) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'verify'");
  }
}
