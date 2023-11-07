package ru.ifmo.backend.util;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class PersonErrorResponse {
  private Map<String, String> message;
  private long timestamp;

  public PersonErrorResponse(Map<String, String> message, long timestamp) {
    this.message = message;
    this.timestamp = timestamp;
  }
}
