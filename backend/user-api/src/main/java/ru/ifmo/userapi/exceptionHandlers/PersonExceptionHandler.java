package ru.ifmo.userapi.exceptionHandlers;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.ifmo.common.responses.ErrorResponse;
import ru.ifmo.userapi.util.ValidException;

@RestControllerAdvice
@Slf4j
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
    log.error(this.getClass().getName() + ": catch errors - " + errors);

    var response = new ErrorResponse(errors, ZonedDateTime.now());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  private ResponseEntity<ErrorResponse> handleException(BadCredentialsException e) {
    var response =
        new ErrorResponse(
            Collections.singletonMap(MESSAGE_FIELD_NAME, e.getMessage()), ZonedDateTime.now());
    log.error(this.getClass().getName() + ": catch error - " + e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }
}
