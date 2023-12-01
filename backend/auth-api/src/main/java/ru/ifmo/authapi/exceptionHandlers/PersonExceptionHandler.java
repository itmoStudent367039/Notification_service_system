package ru.ifmo.authapi.exceptionHandlers;

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

import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

//  @ExceptionHandler
//  private ResponseEntity<String> handleException(HttpClientErrorException e) {
//    return ResponseEntity.status(e.getStatusCode())
//        .contentType(MediaType.APPLICATION_JSON)
//        .body(e.getResponseBodyAsString());
//  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(BadCredentialsException e) {
    var response =
        new ErrorResponse(
            Collections.singletonMap(MESSAGE_FIELD_NAME, e.getMessage()), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(DomainNotExists e) {
    var response =
        new ErrorResponse(
            Collections.singletonMap(FIELD_EMAIL_STRING, e.getMessage()), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(UnknownHostException e) {
    var response =
        new ErrorResponse(
            Collections.singletonMap(FIELD_EMAIL_STRING, e.getMessage()), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // TODO: Что это?
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  private ResponseEntity<ErrorResponse> handleException() {
    var response =
        new ErrorResponse(Collections.singletonMap(MESSAGE_FIELD_NAME, "id"), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
