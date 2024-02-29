package ru.ifmo.authapi.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ru.ifmo.common.dto.CreationDTO;
import ru.ifmo.common.dto.LoginDTO;
import ru.ifmo.common.dto.RegistrationDTO;
import ru.ifmo.authapi.util.validators.RegistrationValidator;
import ru.ifmo.authapi.email.EmailUtils;
import ru.ifmo.authapi.requests.RequestDirector;
import ru.ifmo.authapi.security.JwtUtil;
import ru.ifmo.authapi.user.Person;
import ru.ifmo.authapi.user.PersonDetails;
import ru.ifmo.authapi.util.ObjectConverter;
import ru.ifmo.authapi.util.exceptions.ValidException;
import ru.ifmo.authapi.util.validators.BindingChecker;
import ru.ifmo.common.mail.Mail;
import ru.ifmo.common.responses.ErrorResponse;
import ru.ifmo.common.responses.PersonView;
import ru.ifmo.common.responses.UserInfo;

@Service
@Slf4j
public class AuthenticationService {
  private static final String SUCCESSFULLY_LOGIN = "successfully login";
  private static final String SUCCESSFULLY_REGISTER = "successfully register";
  private static final String SUCCESSFULLY_RESEND = "successfully resend a token";
  private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
  private static final String UNKNOWN_USER = "User";
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final RegistrationValidator registrationValidator;
  private final BindingChecker checker;
  private final ObjectConverter objectConverter;
  private final PeopleService peopleService;
  private final JwtUtil jwtUtil;
  private final RequestDirector requestDirector;

  @Value("${server.host}")
  private String host;

  @Autowired
  public AuthenticationService(
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      RegistrationValidator registrationValidator,
      BindingChecker checker,
      ObjectConverter objectConverter,
      PeopleService peopleService,
      JwtUtil jwtUtil,
      RequestDirector requestDirector) {
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.registrationValidator = registrationValidator;
    this.checker = checker;
    this.objectConverter = objectConverter;
    this.peopleService = peopleService;
    this.jwtUtil = jwtUtil;
    this.requestDirector = requestDirector;
  }

  public ResponseEntity<?> register(RegistrationDTO registrationDTO, BindingResult bindingResult)
      throws ValidException {

    this.validateRegistration(registrationDTO, bindingResult);

    String token = jwtUtil.generateTokenWithConfirmExpirationTime(registrationDTO.getEmail());

    try {
      this.sendMessageToMailService(
          registrationDTO.getUsername(), registrationDTO.getEmail(), token);
      this.savePerson(registrationDTO);
      log.info(
          String.format(
              "(Registration) Successfully register user with email - %s",
              registrationDTO.getEmail()));
      return ResponseEntity.status(HttpStatus.CREATED)
          .contentType(MediaType.APPLICATION_JSON)
          .body(
              HttpResponse.builder()
                  .message(SUCCESSFULLY_REGISTER)
                  .timestamp(ZonedDateTime.now())
                  .build());
    } catch (HttpClientErrorException e) {
      log.error(
          String.format(
              "(Registration) Catch exception while sending request to mail-sender: code - %s; message - %s",
              e.getStatusCode(), e.getMessage()));
      return this.extractResponseFromException(e);
    }
  }

  public ResponseEntity<?> login(LoginDTO loginDTO, BindingResult bindingResult)
      throws ValidException {
    checker.throwIfBindResultHasErrors(bindingResult);

    Person person = this.authenticateAndReturnUser(loginDTO);
    String token = jwtUtil.generateTokenWithCommonUserTime(person.getEmail());

    log.info(String.format("(Login) Success authentication - %s", person.getEmail()));

    try {
      return this.getCurrentUserFromUserServiceAndFormResponse(token, person.getRole().name());
    } catch (HttpClientErrorException e) {
      log.error(
          String.format(
              "(Login) Catch exception while sending request to user-api (get current user): code - %s; message - %s",
              e.getStatusCode(), e.getMessage()));
      return this.extractResponseFromException(e);
    }
  }

  public void confirmAccount(String token)
      throws JWTVerificationException,
          UsernameNotFoundException,
          HttpClientErrorException,
          HttpServerErrorException {

    String email = jwtUtil.validateTokenAndRetrieveClaim(token);
    Optional<Person> personOptional = peopleService.findByEmail(email);

    if (personOptional.isPresent()) {
      Person person = personOptional.get();
      person.setEnable(true);
      peopleService.update(person);
      CreationDTO dto = new CreationDTO(person.getUsername(), person.getEmail());
      requestDirector.sendUserApiCreationRequest(dto, token);
    } else {
      throw new UsernameNotFoundException(email);
    }
  }

  public ResponseEntity<?> resendConfirmToken(LoginDTO loginDTO, BindingResult bindingResult)
      throws ValidException, UsernameNotFoundException {

    checker.throwIfBindResultHasErrors(bindingResult);

    this.validatePerson(loginDTO, bindingResult);

    String token = jwtUtil.generateTokenWithConfirmExpirationTime(loginDTO.getEmail());

    try {
      this.sendMessageToMailService(UNKNOWN_USER, loginDTO.getEmail(), token);

      return ResponseEntity.status(HttpStatus.OK)
          .contentType(MediaType.APPLICATION_JSON)
          .body(
              HttpResponse.builder()
                  .timestamp(ZonedDateTime.now())
                  .message(SUCCESSFULLY_RESEND)
                  .build());
    } catch (HttpClientErrorException e) {
      log.error(
          String.format(
              "(ResendConfirmToken) Catch exception while sending request to mail-sender: code - %s; message - %s",
              e.getStatusCode(), e.getMessage()));
      return this.extractResponseFromException(e);
    }
  }

  public ResponseEntity<UserInfo> authenticatePerson() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails details = (PersonDetails) authentication.getPrincipal();
    log.info(
        String.format(
            "(AuthenticatePerson) Successfully authentication, email - %s",
            details.getPerson().getEmail()));

    return ResponseEntity.ok(
        UserInfo.builder()
            .email(details.getUsername())
            .role(
                Objects.requireNonNull(details.getAuthorities().stream().findAny().orElse(null))
                    .getAuthority())
            .build());
  }

  public ResponseEntity<Void> deleteUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails details = (PersonDetails) authentication.getPrincipal();
    this.peopleService.delete(details.getPerson());
    log.info(
        String.format("(DeleteUser) Delete user with email: %s", details.getPerson().getEmail()));
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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

  private ResponseEntity<ErrorResponse> extractResponseFromException(HttpClientErrorException e) {
    ErrorResponse errorResponse = e.getResponseBodyAs(ErrorResponse.class);
    return ResponseEntity.status(e.getStatusCode())
        .contentType(MediaType.APPLICATION_JSON)
        .body(errorResponse);
  }

  private void sendMessageToMailService(String receiverName, String receiverEmail, String token)
      throws HttpClientErrorException {
    String emailMessage = EmailUtils.getEmailMessage(receiverName, host, token);
    Mail mail =
        Mail.builder()
            .to(receiverEmail)
            .subject(NEW_USER_ACCOUNT_VERIFICATION)
            .message(emailMessage)
            .build();
    requestDirector.sendMessageToMailService(mail);
  }

  private ResponseEntity<HttpResponse> getCurrentUserFromUserServiceAndFormResponse(
      String token, final String role) throws HttpClientErrorException {
    ResponseEntity<PersonView> response = requestDirector.getCurrentPerson(token);
    var message = Optional.ofNullable(response.getBody());
    message.ifPresent((person) -> person.setPersonRole(role));

    return ResponseEntity.status(response.getStatusCode())
        .contentType(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, token)
        .body(
            HttpResponse.builder()
                .data(message.get())
                .message(SUCCESSFULLY_LOGIN)
                .timestamp(ZonedDateTime.now())
                .build());
  }

  private Person authenticateAndReturnUser(LoginDTO loginDTO) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

    return ((PersonDetails) authentication.getPrincipal()).getPerson();
  }

  private void validateRegistration(RegistrationDTO registrationDTO, BindingResult bindingResult)
      throws ValidException {
    registrationValidator.validate(registrationDTO, bindingResult);
    checker.throwIfBindResultHasErrors(bindingResult);
  }

  private void savePerson(RegistrationDTO registrationDTO) {
    Person converted = objectConverter.convertToObject(registrationDTO, Person.class);
    converted.setPassword(passwordEncoder.encode(converted.getPassword()));
    peopleService.save(converted);
  }
}
