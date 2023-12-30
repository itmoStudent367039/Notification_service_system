package ru.ifmo.userapi.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.userapi.services.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
  private final AdminService adminService;

  @PostMapping
  public ResponseEntity<?> notifyUsers(@RequestParam String message) {
    adminService.notifyUsers(message);
    return ResponseEntity.ok().build();
  }
}
