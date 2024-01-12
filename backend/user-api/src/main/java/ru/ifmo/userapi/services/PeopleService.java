package ru.ifmo.userapi.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;
import ru.ifmo.common.models.Person;
import ru.ifmo.common.repositories.PeopleRepository;
import ru.ifmo.common.responses.PersonView;
import ru.ifmo.common.dto.CreationDTO;
import ru.ifmo.userapi.requests.RequestDirector;
import ru.ifmo.userapi.security.JwtUtil;
import ru.ifmo.userapi.util.ObjectConverter;
import ru.ifmo.userapi.util.ValidException;
import ru.ifmo.userapi.validators.BindingChecker;
import ru.ifmo.userapi.validators.CreationValidator;

@Service
@Transactional(readOnly = true)
@Slf4j
public class PeopleService {
  private final ObjectConverter converter;
  private final PeopleRepository repository;
  private final CreationValidator validator;
  private final BindingChecker bindingChecker;
  private final RequestDirector requestDirector;
  private final JwtUtil jwtUtil;

  @Autowired
  public PeopleService(
      ObjectConverter converter,
      PeopleRepository repository,
      CreationValidator validator,
      BindingChecker bindingChecker,
      RequestDirector requestDirector,
      JwtUtil jwtUtil) {
    this.converter = converter;
    this.repository = repository;
    this.validator = validator;
    this.bindingChecker = bindingChecker;
    this.requestDirector = requestDirector;
    this.jwtUtil = jwtUtil;
  }

  @Transactional
  public ResponseEntity<Void> createUser(CreationDTO dto, BindingResult result)
      throws ValidException {

    validator.validate(dto, result);
    bindingChecker.throwIfBindResultHasErrors(result);

    Person converted = converter.convertToObject(dto, Person.class);
    repository.save(converted);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  public ResponseEntity<PersonView> getCurrentPerson() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = (String) authentication.getPrincipal();

    Optional<Person> current = repository.findByEmail(email);

    return current
        .map(
            person ->
                ResponseEntity.status(HttpStatus.OK)
                    .body(converter.convertToObject(person, PersonView.class)))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @Transactional
  public ResponseEntity<Void> deleteUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = (String) authentication.getPrincipal();

    Optional<Person> optional = repository.findByEmail(email);

    log.info(String.format("Try to find user by email: %s", email));

    log.info(optional.map(String.class::cast).orElse("Can not find user with this email"));

    try {
      String jwtToken = jwtUtil.generateTokenWithCommonUserTime(email);
      this.requestDirector.sendAuthApiDeleteRequest(jwtToken);
      optional.ifPresent(this.repository::delete);
      log.info(String.format("Invoke method delete (repository) with email %s", email));
    } catch (HttpClientErrorException e) {
      log.warn(
          String.format(
              "Catch error while sending delete request to auth-api with code: %s; text: %s",
              e.getStatusCode(), e.getMessage()));
      log.warn("User wasn't deleted");
      return ResponseEntity.status(e.getStatusCode()).build();
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  public List<Person> getPeople() {
    return repository.findAll();
  }

  public List<Person> getPeopleExceptOne(String email) {
    return getPeople().stream()
        .filter(person -> !email.equals(person.getEmail()))
        .collect(Collectors.toList());
  }
}
