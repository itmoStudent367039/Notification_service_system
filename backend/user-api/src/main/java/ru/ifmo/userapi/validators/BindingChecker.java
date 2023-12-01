package ru.ifmo.userapi.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.ifmo.userapi.util.ValidException;

@Component
public class BindingChecker {
  public void throwIfBindResultHasErrors(BindingResult bindingResult) throws ValidException {
    if (bindingResult.hasErrors()) {
      throw new ValidException(bindingResult);
    }
  }
}
