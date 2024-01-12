package ru.ifmo.common.responses;

import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

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
