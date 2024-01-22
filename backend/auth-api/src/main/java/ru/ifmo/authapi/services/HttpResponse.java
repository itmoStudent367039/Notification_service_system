package ru.ifmo.authapi.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.ifmo.common.responses.PersonView;

@SuperBuilder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {
  private ZonedDateTime timestamp;
  private String message;
  private PersonView data;
}
