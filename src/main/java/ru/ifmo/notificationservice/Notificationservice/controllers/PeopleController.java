package ru.ifmo.notificationservice.Notificationservice.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.notificationservice.Notificationservice.dto.PersonView;
import ru.ifmo.notificationservice.Notificationservice.dto.UpdateDTO;
import ru.ifmo.notificationservice.Notificationservice.models.Person;
import ru.ifmo.notificationservice.Notificationservice.security.JwtUtil;
import ru.ifmo.notificationservice.Notificationservice.security.PersonDetails;
import ru.ifmo.notificationservice.Notificationservice.services.PeopleService;
import ru.ifmo.notificationservice.Notificationservice.util.*;
import ru.ifmo.notificationservice.Notificationservice.validators.UpdateDTOValidator;


@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PeopleMapper mapper;
    private final ResponseConstructor constructor;
    private final ObjectConverter objectConverter;
    private final BindingChecker checker;
    private final JwtUtil jwtUtil;
    private final UpdateDTOValidator updateDTOValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PeopleMapper mapper, ResponseConstructor constructor, ObjectConverter objectConverter, BindingChecker checker, JwtUtil jwtUtil, UpdateDTOValidator updateDTOValidator) {
        this.peopleService = peopleService;
        this.mapper = mapper;
        this.constructor = constructor;
        this.objectConverter = objectConverter;
        this.checker = checker;
        this.jwtUtil = jwtUtil;
        this.updateDTOValidator = updateDTOValidator;
    }

    @PatchMapping("/update")
    public ResponseEntity<PersonView> update(@RequestBody @Valid UpdateDTO updateDTO, BindingResult result) throws ValidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();

        mapper.updatePersonFromUpdateDTO(updateDTO, person);
        updateDTOValidator.validate(person, result);
        checker.throwIfBindResultHasErrors(result);

        Person updatedPerson = peopleService.update(person);

        return constructor.makeOkResponse(
                objectConverter.convertToObject(updatedPerson, PersonView.class),
                jwtUtil.generateToken(updatedPerson.getUsername())
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        peopleService.delete(personDetails.getPerson());
        return ResponseEntity.noContent().build();
    }

}
