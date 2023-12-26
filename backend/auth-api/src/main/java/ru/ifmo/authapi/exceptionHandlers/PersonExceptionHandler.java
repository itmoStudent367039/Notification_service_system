package ru.ifmo.authapi.exceptionHandlers;

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
import ru.ifmo.authapi.util.exceptions.ValidException;

@RestControllerAdvice
public class PersonExceptionHandler {
  public static final String MESSAGE_FIELD_NAME = "error";

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
    return this.createSingletonErrorResponse(e.getMessage());
  }

  // TODO: Что это?
  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  private ResponseEntity<ErrorResponse> handleException() {
    var response =
        new ErrorResponse(Collections.singletonMap(MESSAGE_FIELD_NAME, "id"), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorResponse> createSingletonErrorResponse(String message) {
    var response =
        new ErrorResponse(
            Collections.singletonMap(PersonExceptionHandler.MESSAGE_FIELD_NAME, message),
            ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }
}
