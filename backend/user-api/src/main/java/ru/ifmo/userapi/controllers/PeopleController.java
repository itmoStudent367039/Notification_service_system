package ru.ifmo.userapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.common.dto.UpdateDTO;
import ru.ifmo.common.responses.PersonView;
import ru.ifmo.common.dto.CreationDTO;
import ru.ifmo.userapi.services.PeopleService;
import ru.ifmo.userapi.util.ValidException;

@RestController
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService service;

  @Autowired
  public PeopleController(PeopleService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestBody CreationDTO creationDTO, BindingResult result)
      throws ValidException {
    return service.createUser(creationDTO, result);
  }

  @GetMapping
  public ResponseEntity<PersonView> getCurrentPerson() {
    return service.getCurrentPerson();
  }

  @PatchMapping
  public ResponseEntity<?> updatePerson(@RequestBody UpdateDTO updateDTO, BindingResult result) throws ValidException {
    return service.updatePerson(updateDTO, result);
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteUser() {
    return service.deleteUser();
  }
}
