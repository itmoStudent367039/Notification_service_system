package ru.ifmo.mailsender.validators;

public class DomainNotExists extends RuntimeException {
  public DomainNotExists(String message) {
    super(message);
  }
}
