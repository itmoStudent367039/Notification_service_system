package ru.ifmo.userapi.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.common.responses.Message;
import ru.ifmo.userapi.services.AdminService;
import ru.ifmo.userapi.util.ValidException;
import ru.ifmo.userapi.validators.BindingChecker;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;
  private final BindingChecker checker;

  @PostMapping
  public ResponseEntity<?> notifyUsers(@RequestBody @Valid Message message, BindingResult result)
      throws ValidException {
    checker.throwIfBindResultHasErrors(result);
    adminService.notifyUsers(message);
    return ResponseEntity.ok().build();
  }
}
