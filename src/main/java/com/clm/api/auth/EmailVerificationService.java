package com.clm.api.auth;

import com.clm.api.user.EmailVerificationToken;
import com.clm.api.user.User;
import com.clm.api.user.UserRepository;
import com.clm.api.utils.Base64Encryption;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

/** EmailOtpService */
@Service
@lombok.RequiredArgsConstructor
public class EmailVerificationService implements TwoStepVerificationService {
  private static final long serialVersionUID = 1L;

  private final UserRepository userRepository;

  @Value("${email.host}")
  private String HOST;

  @Value("${email.port}")
  private int PORT;

  @Value("${email.username}")
  private String APP_EMAIL;

  @Value("${email.password}")
  private String APP_PASSWORD;

  @Value("${clm.api.url.v1}")
  private String API_V1_URL;

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

      if (!props.containsKey("subject")) {
        System.out.println("Error at SendingEmail.java: Required props.subject is missing");
        throw new IllegalArgumentException("Required props.subject is missing");
      }
      if (!props.containsKey("body")) {
        System.out.println("Error at SendingEmail.java: Required props.body is missing");
        throw new IllegalArgumentException("Required props.body is missing");
      }

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
    String token = props.get("token").toString();
    if (token == null || token.isEmpty()) {
      System.out.println("Error at SendingEmail.java: Required props.token is missing");
      throw new IllegalArgumentException("Required props.token is missing");
    }

    String email = Base64Encryption.decodeBetter(identifier);

    User user = userRepository.findByEmail(email).orElse(null);
    if (user == null) {
      return false;
    }

    if (user.getEmailVerificationToken().isValid(token)) {
      user.setStatus(User.Status.ACTIVE);
      return userRepository.save(user) != null;
    }
    return false;
  }

  public String generateVerificationLink(
      String identifier, EmailVerificationToken emailVerificationToken) {
    String link =
        API_V1_URL
            + "/v1/auth/two-step-verification/email?token="
            + UriEncoder.encode(emailVerificationToken.getToken())
            + "&email="
            + UriEncoder.encode(Base64Encryption.encodeBetter(identifier));

    return link;
  }
}
