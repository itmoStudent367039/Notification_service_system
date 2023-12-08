package ru.ifmo.authapi.exceptionHandlers;

import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.ifmo.authapi.responses.ErrorResponse;
import ru.ifmo.authapi.util.exceptions.DomainNotExists;
import ru.ifmo.authapi.util.exceptions.ValidException;

@RestControllerAdvice
public class PersonExceptionHandler {
  public static final String MESSAGE_FIELD_NAME = "error";
  private static final String FIELD_EMAIL_STRING = "email";

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(ValidException e) {
    Map<String, String> errors = new HashMap<>();

    e.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String field = ((FieldError) error).getField();
              String message = error.getDefaultMessage();
              errors.put(field, message);
            });

    var response = new ErrorResponse(errors, ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // TODO: Rest Template exc
  //  @ExceptionHandler
  //  private ResponseEntity<String> handleException(HttpClientErrorException e) {
  //    return ResponseEntity.status(e.getStatusCode())
  //        .contentType(MediaType.APPLICATION_JSON)
  //        .body(e.getResponseBodyAsString());
  //  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(BadCredentialsException e) {
    return this.createSingletonErrorResponse(
        MESSAGE_FIELD_NAME, e.getMessage(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(DomainNotExists e) {
    return this.createSingletonErrorResponse(
        FIELD_EMAIL_STRING, e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
    return this.createSingletonErrorResponse(
        FIELD_EMAIL_STRING, e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(UnknownHostException e) {
    return this.createSingletonErrorResponse(
        FIELD_EMAIL_STRING, e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  // TODO: Что это?
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  private ResponseEntity<ErrorResponse> handleException() {
    var response =
        new ErrorResponse(Collections.singletonMap(MESSAGE_FIELD_NAME, "id"), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorResponse> createSingletonErrorResponse(
      String key, String message, HttpStatus status) {
    var response = new ErrorResponse(Collections.singletonMap(key, message), ZonedDateTime.now());

    return new ResponseEntity<>(response, status);
  }
}
