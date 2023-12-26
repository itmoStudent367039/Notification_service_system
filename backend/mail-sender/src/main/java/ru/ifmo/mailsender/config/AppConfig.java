package ru.ifmo.mailsender.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ifmo.mailsender.validators.BindingChecker;
import ru.ifmo.mailsender.validators.DomainValidator;

@Configuration
public class AppConfig {
  @Bean
  public BindingChecker bindingChecker() {
    return new BindingChecker();
  }

  @Bean
  public DomainValidator domainValidator() {
    return new DomainValidator();
  }
}
