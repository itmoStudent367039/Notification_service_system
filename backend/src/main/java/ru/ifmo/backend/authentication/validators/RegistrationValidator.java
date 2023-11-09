package ru.ifmo.backend.authentication.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.backend.authentication.dto.RegistrationDTO;
import ru.ifmo.backend.user.services.PeopleService;
import ru.ifmo.backend.user.Person;

import java.util.Optional;

@Component
public class RegistrationValidator implements Validator {
  private final PeopleService peopleService;

  @Autowired
  public RegistrationValidator(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return RegistrationDTO.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    RegistrationDTO registrationDTO = (RegistrationDTO) target;

    Optional<Person> personByName = peopleService.findByUsername(registrationDTO.getUsername());
    Optional<Person> personByEmail = peopleService.findByEmail(registrationDTO.getEmail());

    if (personByName.isPresent()) {
      errors.rejectValue("username", "", "This username already reserved");
    }

    if (personByEmail.isPresent()) {
      errors.rejectValue("email", "", "This email already reserved");
    }
  }
}
