package ru.ifmo.authapi.email;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendConfirmAccountMessage(String name, String to, String token) throws MailException;
}
