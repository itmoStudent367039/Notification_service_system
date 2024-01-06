package ru.ifmo.noticesworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("ru.ifmo.*")
@EnableJpaRepositories("ru.ifmo.*")
public class NoticesWorkerApplication {

  public static void main(String[] args) {
    SpringApplication.run(NoticesWorkerApplication.class, args);
  }
}
