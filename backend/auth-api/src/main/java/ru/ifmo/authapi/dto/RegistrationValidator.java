package ru.ifmo.authapi.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.authapi.services.PeopleService;
import ru.ifmo.authapi.user.Person;

import java.util.Optional;

@Component
public class RegistrationValidator implements Validator {
  private final PeopleService peopleService;

  @Autowired
  public RegistrationValidator(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return RegistrationDTO.class.equals(clazz);
  }

  @Override
  public void validate(@NonNull Object target, @NonNull Errors errors) {
    RegistrationDTO registrationDTO = (RegistrationDTO) target;

    Optional<Person> personByEmail = peopleService.findByEmail(registrationDTO.getEmail());

    if (personByEmail.isPresent()) {
      errors.rejectValue("email", "", "This email already reserved");
    }
  }
}
