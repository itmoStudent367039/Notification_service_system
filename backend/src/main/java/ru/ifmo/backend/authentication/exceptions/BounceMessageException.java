package ru.ifmo.backend.authentication.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class BounceMessageException extends RuntimeException {
  private final List<String> messages;

  public BounceMessageException(List<String> messages) {
    this.messages = messages;
  }
}
