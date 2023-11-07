package ru.ifmo.backend.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.backend.dto.TelegramChatIdDTO;
import ru.ifmo.backend.dto.UpdateDTO;
import ru.ifmo.backend.dto.VkIdDTO;
import ru.ifmo.backend.security.JwtUtil;
import ru.ifmo.backend.security.PersonDetails;
import ru.ifmo.backend.services.PeopleService;
import ru.ifmo.backend.util.BindingChecker;
import ru.ifmo.backend.util.ObjectConverter;
import ru.ifmo.backend.util.PeopleMapper;
import ru.ifmo.backend.util.ValidException;
import ru.ifmo.backend.validators.TelegramChatIdDTOValidator;
import ru.ifmo.backend.validators.UpdateDTOValidator;
import ru.ifmo.backend.validators.VkIdDTOValidator;
import ru.ifmo.backend.dto.PersonView;
import ru.ifmo.backend.models.Person;

@RestController
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService peopleService;
  private final PeopleMapper mapper;
  private final ResponseConstructor constructor;
  private final ObjectConverter objectConverter;
  private final BindingChecker checker;
  private final JwtUtil jwtUtil;
  private final UpdateDTOValidator updateDTOValidator;
  private final VkIdDTOValidator vkIdDTOValidator;
  private final TelegramChatIdDTOValidator telegramChatIdDTOValidator;

  @Autowired
  public PeopleController(
      PeopleService peopleService,
      PeopleMapper mapper,
      ResponseConstructor constructor,
      ObjectConverter objectConverter,
      BindingChecker checker,
      JwtUtil jwtUtil,
      UpdateDTOValidator updateDTOValidator,
      VkIdDTOValidator vkIdDTOValidator,
      TelegramChatIdDTOValidator telegramChatIdDTOValidator) {
    this.peopleService = peopleService;
    this.mapper = mapper;
    this.constructor = constructor;
    this.objectConverter = objectConverter;
    this.checker = checker;
    this.jwtUtil = jwtUtil;
    this.updateDTOValidator = updateDTOValidator;
    this.vkIdDTOValidator = vkIdDTOValidator;
    this.telegramChatIdDTOValidator = telegramChatIdDTOValidator;
  }

  @PatchMapping("/update")
  public ResponseEntity<PersonView> update(
          @RequestBody @Valid UpdateDTO updateDTO, BindingResult result) throws ValidException {
    Person person = this.getAuthPersonFromSecurityContext();

    mapper.updatePersonFromUpdateDTO(updateDTO, person);
    updateDTOValidator.validate(person, result);
    checker.throwIfBindResultHasErrors(result);

    return this.updatePersonReturningOkResponse(person);
  }

  @PatchMapping("/set-vk-id")
  public ResponseEntity<PersonView> setVkId(@RequestBody @Valid VkIdDTO idDTO, BindingResult result)
      throws ValidException {
    Person person = this.getAuthPersonFromSecurityContext();

    mapper.updatePersonFromVkIdDTO(idDTO, person);
    vkIdDTOValidator.validate(person, result);
    checker.throwIfBindResultHasErrors(result);

    return this.updatePersonReturningOkResponse(person);
  }

  @PatchMapping("/set-tg-chat")
  public ResponseEntity<PersonView> setTgChatId(
          @RequestBody @Valid TelegramChatIdDTO idDTO, BindingResult result) throws ValidException {
    Person person = this.getAuthPersonFromSecurityContext();
    mapper.updatePersonFromTelegramChatIdDTO(idDTO, person);
    telegramChatIdDTOValidator.validate(person, result);
    checker.throwIfBindResultHasErrors(result);

    return this.updatePersonReturningOkResponse(person);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<HttpStatus> delete() {
    Person person = this.getAuthPersonFromSecurityContext();
    peopleService.delete(person);

    return ResponseEntity.noContent().build();
  }

  private ResponseEntity<PersonView> updatePersonReturningOkResponse(Person person) {
    Person updatedPerson = peopleService.update(person);

    return constructor.makeOkResponse(
        objectConverter.convertToObject(updatedPerson, PersonView.class),
        jwtUtil.generateToken(updatedPerson.getUsername()));
  }

  private Person getAuthPersonFromSecurityContext() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

    return personDetails.getPerson();
  }
}
