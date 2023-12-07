package ru.ifmo.userapi.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {
  private ZonedDateTime timestamp;
  private String message;
  private PersonView data;
}
