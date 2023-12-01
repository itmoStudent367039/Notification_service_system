package ru.ifmo.userapi.util;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class ValidException extends BindException {

  public ValidException(BindingResult bindingResult) {
    super(bindingResult);
  }
}
