package ru.ifmo.backend.authentication.responses;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class ResponseConstructor {
  public ResponseEntity<HttpResponse> buildResponseEntityWithToken(
      HttpResponse body, String token, HttpStatus httpStatus) {

    return ResponseEntity.status(httpStatus).header(HttpHeaders.AUTHORIZATION, token).body(body);
  }

  public HttpResponse buildHttpResponseWithView(PersonView view, String message) {
    return buildHttpResponse(view, message);
  }

  public HttpResponse buildHttpResponseWithoutView(String message) {
    return buildHttpResponse(null, message);
  }

  private HttpResponse buildHttpResponse(PersonView view, String message) {
    return HttpResponse.builder()
        .data(view)
        .message(message)
        .timestamp(ZonedDateTime.now())
        .build();
  }
}
