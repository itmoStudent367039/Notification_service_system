package ru.ifmo.userapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ifmo.userapi.requests.RequestDirector;
import ru.ifmo.userapi.requests.UserInfo;

@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
  private final RequestDirector requestDirector;

  @Autowired
  public AuthenticationFilter(RequestDirector requestDirector) {
    this.requestDirector = requestDirector;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String jwt = extractJwtFromHeader(request);
    if (StringUtils.hasText(jwt)) {

      try {
        log.info("Registered request with jwt: " + jwt);
        log.info("Send request to auth-api to authenticate user");
        ResponseEntity<UserInfo> authResponse = requestDirector.sendAuthApiAuthenticateRequest(jwt);
        log.info("Response with code: " + authResponse.getStatusCode());

        if (authResponse.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
          Optional<UserInfo> info = Optional.ofNullable(authResponse.getBody());
          info.ifPresent(this::authenticateUser);
        }

      } catch (RestClientException e) {
        log.error(String.format("Catch RestClientException: message - %s", e.getMessage()));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    } else {
      log.warn("Request with empty jwt-token");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    filterChain.doFilter(request, response);
  }

  private String extractJwtFromHeader(HttpServletRequest httpRequest) {

    String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      return authHeader.split(" ")[1].trim();
    }

    return null;
  }

  private void authenticateUser(UserInfo userInfo) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userInfo.getEmail(),
            null,
            Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole())));

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    log.info(String.format("Set authentication with email: %s", userInfo.getEmail()));
  }
}
