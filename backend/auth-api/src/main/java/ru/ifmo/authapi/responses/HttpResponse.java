package ru.ifmo.authapi.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HttpResponse {
  private ZonedDateTime timestamp;
  private String message;
  private PersonView personView;
}
