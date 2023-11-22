package ru.ifmo.backend.authentication.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.ifmo.backend.authentication.exceptions.ValidException;

@Component
public class BindingChecker {
  public void throwIfBindResultHasErrors(BindingResult bindingResult) throws ValidException {
    if (bindingResult.hasErrors()) {
      throw new ValidException(bindingResult);
    }
  }
}
