package ru.ifmo.backend.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.backend.dto.RegistrationDTO;
import ru.ifmo.backend.security.JwtUtil;
import ru.ifmo.backend.security.PersonDetails;
import ru.ifmo.backend.services.PeopleService;
import ru.ifmo.backend.util.BindingChecker;
import ru.ifmo.backend.util.ObjectConverter;
import ru.ifmo.backend.util.ValidException;
import ru.ifmo.backend.validators.RegistrationDTOValidator;
import ru.ifmo.backend.dto.LoginDTO;
import ru.ifmo.backend.dto.PersonView;
import ru.ifmo.backend.models.Person;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final RegistrationDTOValidator registrationValidator;
  private final PeopleService peopleService;
  private final AuthenticationManager authenticationManager;
  private final ObjectConverter objectConverter;
  private final BindingChecker checker;
  private final ResponseConstructor constructor;
  private final JwtUtil jwtUtil;

  @Autowired
  public AuthController(
      RegistrationDTOValidator registrationValidator,
      PeopleService peopleService,
      AuthenticationManager authenticationManager,
      ObjectConverter objectConverter,
      BindingChecker checker,
      ResponseConstructor constructor,
      JwtUtil jwtUtil) {
    this.registrationValidator = registrationValidator;
    this.peopleService = peopleService;
    this.constructor = constructor;
    this.authenticationManager = authenticationManager;
    this.objectConverter = objectConverter;
    this.checker = checker;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/registration")
  public ResponseEntity<PersonView> performRegistration(
          @RequestBody @Valid RegistrationDTO registrationDTO, BindingResult result)
      throws ValidException {
    registrationValidator.validate(registrationDTO, result);
    checker.throwIfBindResultHasErrors(result);

    Person converted = objectConverter.convertToObject(registrationDTO, Person.class);

    return constructor.makeOkResponse(
        objectConverter.convertToObject(peopleService.save(converted), PersonView.class),
        jwtUtil.generateToken(converted.getUsername()));
  }

  @PostMapping("/login")
  public ResponseEntity<PersonView> performLogin(
      @RequestBody @Valid LoginDTO loginDTO, BindingResult result) throws ValidException {
    checker.throwIfBindResultHasErrors(result);

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword()));

    Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();

    return constructor.makeOkResponse(
        objectConverter.convertToObject(person, PersonView.class),
        jwtUtil.generateToken(person.getUsername()));
  }
}
