package ru.ifmo.authapi.util.validators;

import org.springframework.validation.BindingResult;
import ru.ifmo.authapi.util.exceptions.ValidException;

public class BindingChecker {
  public void throwIfBindResultHasErrors(BindingResult bindingResult) throws ValidException {
    if (bindingResult.hasErrors()) {
      throw new ValidException(bindingResult);
    }
  }
}
