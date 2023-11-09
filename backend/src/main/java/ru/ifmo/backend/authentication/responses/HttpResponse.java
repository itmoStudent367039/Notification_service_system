package ru.ifmo.backend.authentication.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@SuperBuilder
@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {
  private ZonedDateTime timestamp;
  private String message;
  private PersonView data;
}
