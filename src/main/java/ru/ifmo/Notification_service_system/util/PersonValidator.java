package ru.ifmo.Notification_service_system.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.Notification_service_system.models.Person;
import ru.ifmo.Notification_service_system.services.PeopleService;

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
        Optional<Person> foundUser = peopleService.findByUsername(person.getLogin());

        if (foundUser.isPresent()) {
            errors.rejectValue("username", "", "This username already exists");
        }
    }
}
