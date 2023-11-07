package ru.ifmo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ifmo.backend.dto.PersonView;
import ru.ifmo.backend.security.PersonDetails;
import ru.ifmo.backend.util.ObjectConverter;

@RestController
@RequestMapping("/admin")
public class AdminApi {
  private final ObjectConverter objectConverter;

  @Autowired
  public AdminApi(ObjectConverter objectConverter) {
    this.objectConverter = objectConverter;
  }

  @GetMapping("/index")
  public ResponseEntity<PersonView> doAdminLogic() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
    return ResponseEntity.ok(
        objectConverter.convertToObject(personDetails.getPerson(), PersonView.class));
  }
}
