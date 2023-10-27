package ru.ifmo.notificationservice.Notificationservice.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.notificationservice.Notificationservice.dto.AuthenticationDTO;
import ru.ifmo.notificationservice.Notificationservice.dto.PersonDTO;
import ru.ifmo.notificationservice.Notificationservice.dto.PersonView;
import ru.ifmo.notificationservice.Notificationservice.models.Person;
import ru.ifmo.notificationservice.Notificationservice.security.JwtUtil;
import ru.ifmo.notificationservice.Notificationservice.security.PersonDetails;
import ru.ifmo.notificationservice.Notificationservice.services.PeopleService;
import ru.ifmo.notificationservice.Notificationservice.util.ObjectConverter;
import ru.ifmo.notificationservice.Notificationservice.util.PersonErrorResponse;
import ru.ifmo.notificationservice.Notificationservice.util.PersonValidator;
import ru.ifmo.notificationservice.Notificationservice.util.ValidException;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final PeopleService peopleService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ObjectConverter objectConverter;

    @Autowired
    public AuthController(PersonValidator personValidator, PeopleService peopleService, JwtUtil jwtUtil, AuthenticationManager authenticationManager, ObjectConverter objectConverter) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.objectConverter = objectConverter;
    }

    @PostMapping("/registration")
    public ResponseEntity<PersonView> performRegistration(@RequestBody @Valid PersonDTO personDTO, BindingResult result) throws ValidException {
        Person converted = objectConverter.convertToObject(personDTO, Person.class);
        personValidator.validate(converted, result);
        throwIfBindResultHasErrors(result);

        return makeOkRequest(objectConverter.convertToObject(peopleService.save(converted), PersonView.class));
    }

    @PostMapping("/login")
    public ResponseEntity<PersonView> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult result) throws ValidException {
        throwIfBindResultHasErrors(result);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDTO.getUsername(),
                        authenticationDTO.getPassword()
                )
        );

        Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();

        return makeOkRequest(objectConverter.convertToObject(person, PersonView.class));
    }

    private ResponseEntity<PersonView> makeOkRequest(PersonView view) {
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        jwtUtil.generateToken(view.getUsername())
                )
                .body(view);
    }

    private void throwIfBindResultHasErrors(BindingResult bindingResult) throws ValidException {

        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult);
        }

    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(ValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        var response = new PersonErrorResponse(errors, System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(BadCredentialsException e) {
        var response = new PersonErrorResponse(
                Collections.singletonMap("error", e.getMessage()),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
