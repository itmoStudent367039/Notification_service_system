package ru.ifmo.backend.authentication.exceptions;

public class DomainNotExists extends RuntimeException {
  public DomainNotExists(String message) {
    super(message);
  }
}
