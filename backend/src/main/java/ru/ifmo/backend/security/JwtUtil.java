package ru.ifmo.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {
  @Value("${jwt_secret}")
  private String secret;
  public static final long CONFIRM_EMAIL_TOKEN_LIFE_TIME_MINUTES = 15;
  public static final long USER_TOKEN_LIFE_TIME_MINUTES = 60;

  // TODO: вынести все в константы https://www.javaguides.net/2023/05/spring-boot-spring-security-jwt-mysql.html
  public String generateToken(String email, long lifeTimeMinutes) {
    Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(lifeTimeMinutes).toInstant());

    return JWT.create()
        .withSubject("User details")
        .withClaim("email", email)
        .withIssuedAt(new Date())
        .withIssuer("yestai")
        .withExpiresAt(expirationDate)
        .sign(Algorithm.HMAC256(secret));
  }

  // TODO: вынести все в константы https://www.javaguides.net/2023/05/spring-boot-spring-security-jwt-mysql.html
  public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
    JWTVerifier verifier =
        JWT.require(Algorithm.HMAC256(secret))
            .withSubject("User details")
            .withIssuer("yestai")
            .build();

    DecodedJWT jwt = verifier.verify(token);
    return jwt.getClaim("email").asString();
  }
}
