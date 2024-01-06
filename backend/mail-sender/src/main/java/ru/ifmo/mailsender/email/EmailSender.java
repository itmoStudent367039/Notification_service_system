package ru.ifmo.mailsender.email;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import ru.ifmo.mailsender.models.Mail;

@Service
public interface EmailSender {
  void sendMail(Mail mail) throws MailException;
}
