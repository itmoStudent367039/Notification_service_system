package ru.ifmo.authapi.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ifmo.authapi.services.PersonDetailsService;
import ru.ifmo.authapi.user.PersonDetails;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final PersonDetailsService service;

  @Autowired
  public JwtFilter(JwtUtil jwtUtil, PersonDetailsService service) {
    this.jwtUtil = jwtUtil;
    this.service = service;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException, JWTVerificationException, UsernameNotFoundException {

    String jwt = extractJwtFromHeader(request);
    System.out.println("auth-api (filter): from user jwt - " + jwt);
    if (StringUtils.hasText(jwt)) {
      System.out.println("auth-api (filter): token isn't empty");
      try {
        String email = getEmailFromJwt(jwt);
        System.out.println("auth-api (filter): extract email - " + email);
        UserDetails userDetails = service.loadUserByUsername(email);

        System.out.println(
            "auth-api (filter): receive user - "
                + ((PersonDetails) userDetails).getPerson().getEmail());

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
          this.authenticateUser(userDetails);
        }
      } catch (UsernameNotFoundException | JWTVerificationException ignored) {

      }
    }

    filterChain.doFilter(request, response);
  }

  private String extractJwtFromHeader(HttpServletRequest httpRequest) {

    String authHeader = httpRequest.getHeader("Authorization");

    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      return authHeader.split(" ")[1].trim();
    }

    return null;
  }

  private String getEmailFromJwt(String jwt) throws JWTVerificationException {
    return jwtUtil.validateTokenAndRetrieveClaim(jwt);
  }

  private void authenticateUser(UserDetails userDetails) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    System.out.println("auth-api (filter): set authentication");
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }
}
