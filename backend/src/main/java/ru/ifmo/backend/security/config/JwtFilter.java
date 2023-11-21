package ru.ifmo.backend.security.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ifmo.backend.security.JwtUtil;
import ru.ifmo.backend.user.services.PersonDetailsService;

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
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException, JWTVerificationException, UsernameNotFoundException {

    String jwt = extractJwtFromHeader(request);

    if (StringUtils.hasText(jwt)) {
      String email = getEmailFromJwt(jwt);
      UserDetails userDetails = service.loadUserByUsername(email);

      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        this.authenticateUser(userDetails);
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

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }
}
