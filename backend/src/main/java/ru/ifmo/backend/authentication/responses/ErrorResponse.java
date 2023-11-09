package ru.ifmo.backend.authentication.responses;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Map;

@Setter
@Getter
public class ErrorResponse {
  private Map<String, String> message;
  private ZonedDateTime timestamp;

  public ErrorResponse(Map<String, String> message, ZonedDateTime timestamp) {
    this.message = message;
    this.timestamp = timestamp;
  }
}
