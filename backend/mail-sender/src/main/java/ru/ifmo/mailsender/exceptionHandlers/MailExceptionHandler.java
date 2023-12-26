package ru.ifmo.mailsender.exceptionHandlers;

import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ifmo.mailsender.responses.ErrorResponse;
import ru.ifmo.mailsender.validators.DomainNotExists;
import ru.ifmo.mailsender.validators.ValidException;

@RestControllerAdvice
public class MailExceptionHandler {
  private static final String FIELD_EMAIL_STRING = "email";

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(DomainNotExists e) {
    return this.createSingletonErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {
    return this.createSingletonErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(UnknownHostException e) {
    return this.createSingletonErrorResponse(e.getMessage());
  }

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

  private ResponseEntity<ErrorResponse> createSingletonErrorResponse(String message) {
    var response =
        new ErrorResponse(
            Collections.singletonMap(MailExceptionHandler.FIELD_EMAIL_STRING, message),
            ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
