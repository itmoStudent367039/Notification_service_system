package ru.ifmo.authapi.util.exceptions;

public class DomainNotExists extends RuntimeException {
  public DomainNotExists(String message) {
    super(message);
  }
}
