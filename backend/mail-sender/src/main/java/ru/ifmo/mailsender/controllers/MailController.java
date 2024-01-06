package ru.ifmo.mailsender.controllers;

import jakarta.validation.Valid;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.mailsender.models.Mail;
import ru.ifmo.mailsender.services.MailService;
import ru.ifmo.mailsender.validators.DomainNotExists;
import ru.ifmo.mailsender.validators.ValidException;

@RestController
public class MailController {
  private final MailService mailService;

  @Autowired
  public MailController(MailService mailService) {
    this.mailService = mailService;
  }

  @PostMapping("/send")
  public ResponseEntity<Void> sendEmail(@RequestBody @Valid Mail mail, BindingResult result)
      throws UnknownHostException, DomainNotExists, IllegalArgumentException, ValidException {
    return mailService.sendEmail(mail, result);
  }
}
