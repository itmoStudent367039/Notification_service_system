package ru.ifmo.userapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
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
    System.out.println("user: point 2 - receive -- " + jwt);
    if (StringUtils.hasText(jwt)) {

      try {
        ResponseEntity<UserInfo> authResponse = requestDirector.sendAuthApiAuthenticateRequest(jwt);
        System.out.println("user: point 4 receive " + authResponse.getStatusCode());

        if (authResponse.getStatusCode().isSameCodeAs(HttpStatus.OK)) {

          Optional<UserInfo> info = Optional.ofNullable(authResponse.getBody());
          info.ifPresent(this::authenticateUser);
        }

      } catch (RestClientException e) {
        System.out.println("User-api: Exc from authenticate resp to auth-api -");
        System.out.println(e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    } else {
      System.out.println("empty jwt");
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
    System.out.println("body: " + userInfo.getEmail());
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userInfo.getEmail(),
            null,
            Collections.singletonList(new SimpleGrantedAuthority(userInfo.getRole())));

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    System.out.println("set authentication!");
  }
}
