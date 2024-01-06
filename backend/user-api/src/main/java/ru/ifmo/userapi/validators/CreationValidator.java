package ru.ifmo.userapi.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.common.models.Person;
import ru.ifmo.common.repositories.PeopleRepository;
import ru.ifmo.userapi.dto.CreationDTO;

import java.util.Optional;

@Component
public class CreationValidator implements Validator {
  private final PeopleRepository peopleRepository;

  @Autowired
  public CreationValidator(PeopleRepository peopleRepository) {
    this.peopleRepository = peopleRepository;
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return CreationDTO.class.equals(clazz);
  }

  @Override
  public void validate(@NonNull Object target, @NonNull Errors errors) {
    CreationDTO registrationDTO = (CreationDTO) target;

    Optional<Person> personByName = peopleRepository.findByUsername(registrationDTO.getUsername());
    Optional<Person> personByEmail = peopleRepository.findByEmail(registrationDTO.getEmail());

    if (personByName.isPresent()) {
      errors.rejectValue("username", "", "This username already reserved");
    }

    if (personByEmail.isPresent()) {
      errors.rejectValue("email", "", "This email already reserved");
    }
  }
}
