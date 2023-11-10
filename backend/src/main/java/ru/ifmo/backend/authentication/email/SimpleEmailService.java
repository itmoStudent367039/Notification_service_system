package ru.ifmo.backend.authentication.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static ru.ifmo.backend.authentication.email.EmailUtils.getEmailMessage;

@Service
public class SimpleEmailService implements EmailService {
  private final JavaMailSender emailSender;

  @Value("${spring.mail.verify.host}")
  private String host;

  @Value("${spring.mail.username}")
  private String from;

  private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";

  public SimpleEmailService(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  @Override
  @Async
  public void sendConfirmAccountMessage(String name, String to, String token)
      throws MailException {

    // TODO: check (Is domain of user email exists, and throw error if not)
    SimpleMailMessage message = new SimpleMailMessage();
    message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
    message.setFrom(from);
    message.setTo(to);
    message.setText(getEmailMessage(name, host, token));
    emailSender.send(message);

  }
}
