package ru.ifmo.backend.authentication.validators;

public class DomainNotExists extends RuntimeException {
  public DomainNotExists(String message) {
    super(message);
  }
}
