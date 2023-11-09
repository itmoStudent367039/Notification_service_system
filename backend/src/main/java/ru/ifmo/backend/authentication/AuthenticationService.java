package ru.ifmo.backend.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.ifmo.backend.authentication.dto.LoginDTO;
import ru.ifmo.backend.authentication.dto.RegistrationDTO;
import ru.ifmo.backend.authentication.email.EmailService;
import ru.ifmo.backend.authentication.responses.HttpResponse;
import ru.ifmo.backend.authentication.responses.PersonView;
import ru.ifmo.backend.authentication.responses.ResponseConstructor;
import ru.ifmo.backend.authentication.validators.RegistrationValidator;
import ru.ifmo.backend.security.JwtUtil;
import ru.ifmo.backend.user.Person;
import ru.ifmo.backend.user.PersonDetails;
import ru.ifmo.backend.user.services.PeopleService;
import ru.ifmo.backend.authentication.validators.BindingChecker;
import ru.ifmo.backend.util.ObjectConverter;
import ru.ifmo.backend.authentication.validators.ValidException;

import java.util.Optional;

@Service
public class AuthenticationService {
  private final ResponseConstructor constructor;
  private final AuthenticationManager authenticationManager;
  private final RegistrationValidator registrationValidator;
  private final BindingChecker checker;
  private final ObjectConverter objectConverter;
  private final PeopleService peopleService;
  private final JwtUtil jwtUtil;
  private final EmailService emailService;
  private static final String SUCCESSFULLY_LOGIN = "successfully login";
  private static final String SUCCESSFULLY_REGISTER = "successfully register";

  @Autowired
  public AuthenticationService(
      ResponseConstructor constructor,
      AuthenticationManager authenticationManager,
      RegistrationValidator registrationValidator,
      BindingChecker checker,
      ObjectConverter objectConverter,
      PeopleService peopleService,
      JwtUtil jwtUtil,
      EmailService emailService) {
    this.constructor = constructor;
    this.authenticationManager = authenticationManager;
    this.registrationValidator = registrationValidator;
    this.checker = checker;
    this.objectConverter = objectConverter;
    this.peopleService = peopleService;
    this.jwtUtil = jwtUtil;
    this.emailService = emailService;
  }

  public ResponseEntity<HttpResponse> register(
      RegistrationDTO registrationDTO, BindingResult bindingResult)
      throws ValidException, MailException {

    registrationValidator.validate(registrationDTO, bindingResult);
    checker.throwIfBindResultHasErrors(bindingResult);

    Person saved =
        peopleService.save(objectConverter.convertToObject(registrationDTO, Person.class));

    String token =
        jwtUtil.generateToken(saved.getEmail(), JwtUtil.CONFIRM_EMAIL_TOKEN_LIFE_TIME_MINUTES);

    emailService.sendConfirmAccountMessage(saved.getUsername(), saved.getEmail(), token);

    PersonView view = objectConverter.convertToObject(saved, PersonView.class);

    return constructor.singletonPersonResponse(
        constructor.buildHttpResponse(view, SUCCESSFULLY_REGISTER), token, HttpStatus.CREATED);
  }

  public ResponseEntity<HttpResponse> login(LoginDTO loginDTO, BindingResult bindingResult)
      throws ValidException {

    checker.throwIfBindResultHasErrors(bindingResult);

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

    Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
    String token = jwtUtil.generateToken(person.getEmail(), JwtUtil.USER_TOKEN_LIFE_TIME_MINUTES);

    PersonView view = objectConverter.convertToObject(person, PersonView.class);

    return constructor.singletonPersonResponse(
        constructor.buildHttpResponse(view, SUCCESSFULLY_LOGIN), token, HttpStatus.OK);
  }

  public void confirmAccount(String token)
      throws JWTVerificationException, UsernameNotFoundException {
    // TODO: if token is expired => allow user choose: 1) delete account, 2) resend email with new token
    String email = jwtUtil.validateTokenAndRetrieveClaim(token);
    Optional<Person> personOptional = peopleService.findByEmail(email);

    if (personOptional.isPresent()) {

      Person person = personOptional.get();
      person.setEnable(true);
      peopleService.update(person);

    } else {
      throw new UsernameNotFoundException(email);
    }
  }
}
