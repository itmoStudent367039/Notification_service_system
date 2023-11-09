package ru.ifmo.backend.authentication.email;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendConfirmAccountMessage(String name, String to, String token);
}
