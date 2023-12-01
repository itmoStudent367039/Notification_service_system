package ru.ifmo.authapi.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.Map;

@Setter
@Getter
@SuperBuilder
public class ErrorResponse {
  private Map<String, String> message;
  private ZonedDateTime timestamp;

  public ErrorResponse(Map<String, String> message, ZonedDateTime timestamp) {
    this.message = message;
    this.timestamp = timestamp;
  }
}
