package ru.ifmo.authapi.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.authapi.dto.LoginDTO;
import ru.ifmo.authapi.dto.RegistrationDTO;
import ru.ifmo.authapi.requests.UserInfo;
import ru.ifmo.authapi.services.AuthenticationService;
import ru.ifmo.authapi.util.exceptions.ValidException;

@Controller
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/registration")
  @ResponseBody
  public ResponseEntity<?> performRegistration(
      @RequestBody @Valid RegistrationDTO registrationDTO, BindingResult result)
      throws ValidException {

    return authenticationService.register(registrationDTO, result);
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<?> performLogin(@RequestBody @Valid LoginDTO loginDTO, BindingResult result)
      throws ValidException {

    return authenticationService.login(loginDTO, result);
  }

  @GetMapping("/confirm")
  public String confirmPersonAccount(@RequestParam(name = "token") String token, Model model) {

    try {
      authenticationService.confirmAccount(token);
      return "confirmAccount/success";
    } catch (UsernameNotFoundException e) {
      model.addAttribute("emailNotFound", e.getMessage());
    } catch (JWTVerificationException e) {
      if (e instanceof TokenExpiredException) {
        model.addAttribute("registrationLink", "http://localhost:5432/auth");
      }
      model.addAttribute("tokenError", e.getMessage());
    }

    return "confirmAccount/failed";
  }

  @PostMapping("/resend-token")
  @ResponseBody
  public ResponseEntity<?> resendConfirmToken(
      @RequestBody @Valid LoginDTO loginDTO, BindingResult result)
      throws ValidException, UsernameNotFoundException, BadCredentialsException {

    return authenticationService.resendConfirmToken(loginDTO, result);
  }

  @DeleteMapping("/delete")
  @ResponseBody
  public ResponseEntity<Void> deleteUser() {
    return authenticationService.deleteUser();
  }

  @GetMapping("/authenticate")
  @ResponseBody
  public ResponseEntity<UserInfo> authenticatePerson() {
    return authenticationService.authenticatePerson();
  }
}
