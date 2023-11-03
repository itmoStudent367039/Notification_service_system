package ru.ifmo.notificationservice.Notificationservice.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.notificationservice.Notificationservice.models.Person;
import ru.ifmo.notificationservice.Notificationservice.services.PeopleService;

import java.util.Optional;

@Component
public class UpdateDTOValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public UpdateDTOValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person updateDTO = (Person) target;

        Optional<Person> personByName = peopleService.findByUsername(updateDTO.getUsername());
        Optional<Person> personByEmail = peopleService.findByEmail(updateDTO.getEmail());

        if (personByName.isPresent() && personByName.get().getId() != updateDTO.getId()) {
            errors.rejectValue("username", "", "This username already reserved");
        }

        if (personByEmail.isPresent() && personByEmail.get().getId() != updateDTO.getId()) {
            errors.rejectValue("email", "", "This email already reserved");
        }
    }
}
