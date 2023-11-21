package ru.ifmo.backend.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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
import ru.ifmo.backend.authentication.validators.DomainNotExists;
import ru.ifmo.backend.authentication.validators.ValidException;

import java.net.UnknownHostException;

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
      throws ValidException, UnknownHostException, DomainNotExists {

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
      if (e instanceof TokenExpiredException) {
        model.addAttribute("registrationLink", "http://localhost:5432/auth");
      }
      model.addAttribute("tokenError", e.getMessage());
    }

    return "confirmAccount/failed";
  }
}
