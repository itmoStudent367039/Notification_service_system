package ru.ifmo.userapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.ifmo.userapi.dto.CreationDTO;
import ru.ifmo.userapi.models.Person;
import ru.ifmo.userapi.repositories.PeopleRepository;
import ru.ifmo.userapi.responses.PersonView;
import ru.ifmo.userapi.util.ObjectConverter;
import ru.ifmo.userapi.util.ValidException;
import ru.ifmo.userapi.validators.BindingChecker;
import ru.ifmo.userapi.validators.CreationValidator;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
  private final ObjectConverter converter;
  private final PeopleRepository repository;
  private final CreationValidator validator;
  private final BindingChecker bindingChecker;

  @Autowired
  public PeopleService(
      ObjectConverter converter,
      PeopleRepository repository,
      CreationValidator validator,
      BindingChecker bindingChecker) {
    this.converter = converter;
    this.repository = repository;
    this.validator = validator;
    this.bindingChecker = bindingChecker;
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

  //  public Optional<Person> findByUsername(String username) {
  //    return repository.findByUsername(username);
  //  }
  //
  //  public Optional<Person> findByEmail(String email) {
  //    return repository.findByEmail(email);
  //  }
}
