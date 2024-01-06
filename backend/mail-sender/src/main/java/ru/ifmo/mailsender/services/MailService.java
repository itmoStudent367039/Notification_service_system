package ru.ifmo.mailsender.services;

import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.ifmo.mailsender.email.EmailSender;
import ru.ifmo.mailsender.models.Mail;
import ru.ifmo.mailsender.validators.BindingChecker;
import ru.ifmo.mailsender.validators.DomainNotExists;
import ru.ifmo.mailsender.validators.DomainValidator;
import ru.ifmo.mailsender.validators.ValidException;

@Service
public class MailService {
  private final EmailSender emailSender;
  private final DomainValidator domainValidator;
  private final BindingChecker bindingChecker;

  @Autowired
  public MailService(
      EmailSender emailSender, DomainValidator domainValidator, BindingChecker bindingChecker) {
    this.emailSender = emailSender;
    this.domainValidator = domainValidator;
    this.bindingChecker = bindingChecker;
  }

  public ResponseEntity<Void> sendEmail(Mail mail, BindingResult bindingResult)
      throws UnknownHostException, DomainNotExists, IllegalArgumentException, ValidException {

    this.bindingChecker.throwIfBindResultHasErrors(bindingResult);
    this.domainValidator.throwExceptionIfDomainNotExists(mail.getTo());

    this.emailSender.sendMail(mail);

    return ResponseEntity.ok().build();
  }
}
