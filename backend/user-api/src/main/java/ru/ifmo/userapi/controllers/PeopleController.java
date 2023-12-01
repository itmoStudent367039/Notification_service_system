package ru.ifmo.userapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.userapi.dto.CreationDTO;
import ru.ifmo.userapi.responses.PersonView;
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

  @PostMapping("/create")
  public ResponseEntity<Void> createUser(@RequestBody CreationDTO creationDTO, BindingResult result)
      throws ValidException {

    return service.createUser(creationDTO, result);
  }

  @GetMapping()
  public ResponseEntity<PersonView> getCurrentPerson() {

    return service.getCurrentPerson();
  }
}
