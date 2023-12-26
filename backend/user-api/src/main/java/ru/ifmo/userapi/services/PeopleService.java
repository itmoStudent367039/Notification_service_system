package ru.ifmo.userapi.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.HttpClientErrorException;
import ru.ifmo.userapi.dto.CreationDTO;
import ru.ifmo.userapi.models.Person;
import ru.ifmo.userapi.repositories.PeopleRepository;
import ru.ifmo.userapi.requests.RequestDirector;
import ru.ifmo.userapi.responses.PersonView;
import ru.ifmo.userapi.security.JwtUtil;
import ru.ifmo.userapi.util.ObjectConverter;
import ru.ifmo.userapi.util.ValidException;
import ru.ifmo.userapi.validators.BindingChecker;
import ru.ifmo.userapi.validators.CreationValidator;

@Service
@Transactional(readOnly = true)
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
    System.out.println("Begin method 'deleteUser'");
    System.out.println("Current email - " + email);
    System.out.println("Find user by email");
    System.out.println(optional.isPresent() ? optional.get() : "can not find user with this email");

    try {
      System.out.println("Try send delete request to auth-api");
      String jwtToken = jwtUtil.generateTokenWithCommonUserTime(email);
      this.requestDirector.sendAuthApiDeleteRequest(jwtToken);
      optional.ifPresent(this.repository::delete);
      System.out.println("User was deleted (auth-api, user-api)");
    } catch (HttpClientErrorException e) {
      return ResponseEntity.status(e.getStatusCode()).build();
    }

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  //  public Optional<Person> findByUsername(String username) {
  //    return repository.findByUsername(username);
  //  }
  //
  //  public Optional<Person> findByEmail(String email) {
  //    return repository.findByEmail(email);
  //  }
}
