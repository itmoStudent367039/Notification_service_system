package ru.ifmo.notificationservice.Notificationservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.notificationservice.Notificationservice.models.Person;
import ru.ifmo.notificationservice.Notificationservice.services.PeopleService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        Optional<Person> personByName = peopleService.findByUsername(person.getUsername());
        Optional<Person> personByEmail = peopleService.findByEmail(person.getEmail());

        if (personByName.isPresent()) {
            errors.rejectValue("username", "", "This username already reserved");
        }

        if (personByEmail.isPresent()) {
            errors.rejectValue("email", "", "This email already reserved");
        }

    }
}
