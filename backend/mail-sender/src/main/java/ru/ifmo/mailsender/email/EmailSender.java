package ru.ifmo.mailsender.email;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import ru.ifmo.common.mail.Mail;

@Service
public interface EmailSender {
  void sendMail(Mail mail) throws MailException;
}
