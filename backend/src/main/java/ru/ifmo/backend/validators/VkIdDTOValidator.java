package ru.ifmo.backend.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.backend.models.Person;
import ru.ifmo.backend.services.PeopleService;

import java.util.Objects;
import java.util.Optional;

@Component
public class VkIdDTOValidator implements Validator {
  private final PeopleService peopleService;

  @Autowired
  public VkIdDTOValidator(PeopleService peopleService) {
    this.peopleService = peopleService;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Person.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Person person = (Person) target;
    Optional<Integer> vkId = Optional.ofNullable(person.getVkId());

    if (vkId.isPresent()) {
      Optional<Person> personByVkId = peopleService.findByVkId(person.getVkId());

      if (personByVkId.isPresent()
          && (!Objects.equals(personByVkId.get().getId(), person.getId()))) {
        errors.rejectValue("vkId", "", "This vkId already used");
      }
    } else {
      errors.rejectValue("vkId", "", "vkId is null");
    }
  }
}
