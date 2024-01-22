package ru.ifmo.mailsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MailSenderApplication {

  public static void main(String[] args) {
    SpringApplication.run(MailSenderApplication.class, args);
  }
}
