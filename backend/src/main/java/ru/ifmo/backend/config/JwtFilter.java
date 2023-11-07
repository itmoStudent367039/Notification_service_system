package ru.ifmo.backend.config;

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
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ifmo.backend.security.JwtUtil;
import ru.ifmo.backend.services.PersonDetailsService;

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
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = authHeader.split(" ")[1].trim();

    if (jwt.isBlank()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
      UserDetails userDetails = service.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(
              userDetails, userDetails.getPassword(), userDetails.getAuthorities());

      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      }

    } catch (JWTVerificationException | UsernameNotFoundException e) {

    }

    filterChain.doFilter(request, response);
  }
}
