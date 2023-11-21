package ru.ifmo.backend.authentication.exceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.ifmo.backend.authentication.responses.ErrorResponse;
import ru.ifmo.backend.authentication.validators.DomainNotExists;
import ru.ifmo.backend.authentication.validators.ValidException;

import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class PersonExceptionHandler {
  private static final String MESSAGE_FIELD_NAME = "error";

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
        new ErrorResponse(Collections.singletonMap("email", e.getMessage()), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(UnknownHostException e) {
    var response =
        new ErrorResponse(Collections.singletonMap("email", e.getMessage()), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  private ResponseEntity<ErrorResponse> handleException() {
    var response =
        new ErrorResponse(Collections.singletonMap(MESSAGE_FIELD_NAME, "id"), ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
