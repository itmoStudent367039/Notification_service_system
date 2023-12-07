package ru.ifmo.authapi.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;
import ru.ifmo.authapi.dto.LoginDTO;
import ru.ifmo.authapi.dto.RegistrationDTO;
import ru.ifmo.authapi.dto.RegistrationValidator;
import ru.ifmo.authapi.email.EmailService;
import ru.ifmo.authapi.requests.RequestDirector;
import ru.ifmo.authapi.requests.UserInfo;
import ru.ifmo.authapi.responses.ErrorResponse;
import ru.ifmo.authapi.responses.HttpResponse;
import ru.ifmo.authapi.responses.PersonView;
import ru.ifmo.authapi.security.JwtUtil;
import ru.ifmo.authapi.user.Person;
import ru.ifmo.authapi.user.PersonDetails;
import ru.ifmo.authapi.util.ObjectConverter;
import ru.ifmo.authapi.util.exceptions.ValidException;
import ru.ifmo.authapi.util.validators.BindingChecker;
import ru.ifmo.authapi.util.validators.DomainValidator;

import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final RegistrationValidator registrationValidator;
  private final BindingChecker checker;
  private final ObjectConverter objectConverter;
  private final PeopleService peopleService;
  private final JwtUtil jwtUtil;
  private final EmailService emailService;
  private final DomainValidator domainValidator;
  private final RequestDirector requestDirector;

  private static final String SUCCESSFULLY_LOGIN = "successfully login";
  private static final String SUCCESSFULLY_REGISTER = "successfully register";
  private static final String SUCCESSFULLY_RESEND = "successfully resend a token";
  private static final String UNKNOWN_USER = "User";

  @Autowired
  public AuthenticationService(
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      RegistrationValidator registrationValidator,
      BindingChecker checker,
      ObjectConverter objectConverter,
      PeopleService peopleService,
      JwtUtil jwtUtil,
      EmailService emailService,
      DomainValidator domainValidator,
      RequestDirector requestDirector) {
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.registrationValidator = registrationValidator;
    this.checker = checker;
    this.objectConverter = objectConverter;
    this.peopleService = peopleService;
    this.jwtUtil = jwtUtil;
    this.emailService = emailService;
    this.domainValidator = domainValidator;
    this.requestDirector = requestDirector;
  }

  public ResponseEntity<?> register(RegistrationDTO registrationDTO, BindingResult bindingResult)
      throws ValidException, MailException, UnknownHostException {

    domainValidator.throwExceptionIfDomainNotExists(registrationDTO.getEmail());
    registrationValidator.validate(registrationDTO, bindingResult);
    checker.throwIfBindResultHasErrors(bindingResult);

    Person converted = objectConverter.convertToObject(registrationDTO, Person.class);
    converted.setPassword(passwordEncoder.encode(converted.getPassword()));

    Person saved = peopleService.save(converted);

    String token = jwtUtil.generateTokenWithConfirmExpirationTime(saved.getEmail());

    try {

      requestDirector.sendUserApiCreationRequest(registrationDTO, token);

      emailService.sendConfirmAccountMessage(
          registrationDTO.getUsername(), registrationDTO.getEmail(), token);

    } catch (HttpClientErrorException e) {
      System.out.println("auth: (5) bad - " + e.getStatusCode());
      peopleService.delete(saved);

      ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);

      return ResponseEntity.status(e.getStatusCode())
          .contentType(MediaType.APPLICATION_JSON)
          .body(errorResponse);
    } catch (MailException e) {
// TODO: send delete request to user-api
      peopleService.delete(saved);


      return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
          .body(ErrorResponse.builder().timestamp(ZonedDateTime.now()).build());
    }

    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            HttpResponse.builder()
                .message(SUCCESSFULLY_REGISTER)
                .timestamp(ZonedDateTime.now())
                .build());
  }

  public ResponseEntity<?> login(LoginDTO loginDTO, BindingResult bindingResult)
      throws ValidException {
    System.out.println("Auth-api: start login method");
    checker.throwIfBindResultHasErrors(bindingResult);

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

    Person person = ((PersonDetails) authentication.getPrincipal()).getPerson();
    String token = jwtUtil.generateTokenWithCommonUserTime(person.getEmail());
    System.out.println("Auth-api: success authentication - " + person.getEmail());
    try {
      System.out.println("Auth-api: send get current person request");
      ResponseEntity<PersonView> response = requestDirector.getCurrentPerson(token);

      return ResponseEntity.status(response.getStatusCode())
          .contentType(MediaType.APPLICATION_JSON)
          .header(HttpHeaders.AUTHORIZATION, token)
          .body(
              HttpResponse.builder()
                  .personView(response.getBody())
                  .message(SUCCESSFULLY_LOGIN)
                  .timestamp(ZonedDateTime.now())
                  .build());
    } catch (HttpClientErrorException e) {
      ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);

      return ResponseEntity.status(e.getStatusCode())
          .contentType(MediaType.APPLICATION_JSON)
          .body(errorResponse);
    }
  }

  public void confirmAccount(String token)
      throws JWTVerificationException, UsernameNotFoundException {

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

  public ResponseEntity<HttpResponse> resendConfirmToken(
      LoginDTO loginDTO, BindingResult bindingResult)
      throws ValidException, UsernameNotFoundException {

    checker.throwIfBindResultHasErrors(bindingResult);

    this.validatePerson(loginDTO, bindingResult);

    String token = jwtUtil.generateTokenWithConfirmExpirationTime(loginDTO.getEmail());
    emailService.sendConfirmAccountMessage(UNKNOWN_USER, loginDTO.getEmail(), token);

    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(
            HttpResponse.builder()
                .timestamp(ZonedDateTime.now())
                .message(SUCCESSFULLY_RESEND)
                .build());
  }

  public ResponseEntity<UserInfo> authenticatePerson() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails details = (PersonDetails) authentication.getPrincipal();
    System.out.println("auth: 3 point - receive from user: " + details.getUsername());

    return ResponseEntity.ok(
        UserInfo.builder()
            .email(details.getUsername())
            .role(
                Objects.requireNonNull(details.getAuthorities().stream().findAny().orElse(null))
                    .getAuthority())
            .build());
  }

  private void validatePerson(LoginDTO loginDTO, BindingResult bindingResult)
      throws ValidException {
    Optional<Person> personOptional = peopleService.findByEmail(loginDTO.getEmail());

    if (personOptional.isPresent()) {
      Person person = personOptional.get();
      boolean passwordMatches =
          passwordEncoder.matches(loginDTO.getPassword(), person.getPassword());

      if (!passwordMatches) {
        bindingResult.rejectValue("password", "", "Invalid password");
      }
    } else {
      bindingResult.rejectValue("email", "", "Email not found");
    }

    checker.throwIfBindResultHasErrors(bindingResult);
  }
}
