package ru.ifmo.backend.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.backend.authentication.dto.LoginDTO;
import ru.ifmo.backend.authentication.dto.RegistrationDTO;
import ru.ifmo.backend.authentication.responses.HttpResponse;
import ru.ifmo.backend.authentication.validators.ValidException;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @Autowired
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/registration")
  @ResponseBody
  public ResponseEntity<HttpResponse> performRegistration(
      @RequestBody @Valid RegistrationDTO registrationDTO, BindingResult result)
      throws ValidException {

    return authenticationService.register(registrationDTO, result);
  }

  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<HttpResponse> performLogin(
      @RequestBody @Valid LoginDTO loginDTO, BindingResult result) throws ValidException {

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
      model.addAttribute("tokenError", e.getMessage());
    }

    return "confirmAccount/failed";
  }
}
