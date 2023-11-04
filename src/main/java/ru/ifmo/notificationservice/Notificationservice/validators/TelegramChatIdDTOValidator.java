package ru.ifmo.notificationservice.Notificationservice.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.ifmo.notificationservice.Notificationservice.models.Person;
import ru.ifmo.notificationservice.Notificationservice.services.PeopleService;

import java.util.Objects;
import java.util.Optional;

@Component
public class TelegramChatIdDTOValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public TelegramChatIdDTOValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Long> tgId = Optional.ofNullable(person.getTelegramChatId());

        if (tgId.isPresent()) {
            Optional<Person> personByTelegramChatId = peopleService.findByTelegramChatId(person.getTelegramChatId());

            if (personByTelegramChatId.isPresent() && (!Objects.equals(personByTelegramChatId.get().getId(), person.getId()))) {
                errors.rejectValue("telegramChatId", "", "This telegram chat id already used");
            }
        } else {
            errors.rejectValue("telegramChatId", "", "telegramChatId is null");
        }

    }
}
