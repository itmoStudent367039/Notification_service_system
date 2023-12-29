package ru.ifmo.mailsender.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ifmo.mailsender.models.Mail;

@Service
@Slf4j
public class SimpleEmailSender implements EmailSender {
  private final JavaMailSender emailSender;

  @Value("${spring.mail.username}")
  private String from;

  @Autowired
  public SimpleEmailSender(JavaMailSender emailSender) {
    this.emailSender = emailSender;
  }

  @Override
  @Async
  public void sendMail(Mail mail) throws MailException {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setSubject(mail.getSubject());
    message.setFrom(from);
    message.setTo(mail.getTo());
    message.setText(mail.getMessage());
    emailSender.send(message);
    log.info(String.format("Send email to - %s", mail.getTo()));
  }
}
