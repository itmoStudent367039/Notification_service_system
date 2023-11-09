package ru.ifmo.backend.authentication.responses;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Component
public class ResponseConstructor {
  public ResponseEntity<HttpResponse> singletonPersonResponse(
      HttpResponse body, String token, HttpStatus httpStatus) {

    return ResponseEntity.status(httpStatus).header(HttpHeaders.AUTHORIZATION, token).body(body);
  }

  public HttpResponse buildHttpResponse(PersonView view, String message) {
    return HttpResponse.builder()
        .data(view)
        .message(message)
        .timestamp(ZonedDateTime.now())
        .build();
  }
}
