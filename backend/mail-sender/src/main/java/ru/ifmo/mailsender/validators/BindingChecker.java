package ru.ifmo.mailsender.validators;

import org.springframework.validation.BindingResult;

public class BindingChecker {
  public void throwIfBindResultHasErrors(BindingResult bindingResult) throws ValidException {
    if (bindingResult.hasErrors()) {
      throw new ValidException(bindingResult);
    }
  }
}
