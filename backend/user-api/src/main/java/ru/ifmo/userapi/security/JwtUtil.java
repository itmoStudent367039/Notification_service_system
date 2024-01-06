package ru.ifmo.userapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.jwtSecret}")
  private String secret;

  @Value("${jwt.expirationUserTime}")
  private long userTime;

  @Value("${jwt.issuer}")
  private String issuer;

  @Value("${jwt.subject}")
  private String subject;

  @Value("${jwt.claimName}")
  private String claimName;

  private String generateToken(String email, long lifeTimeMinutes) {
    Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(lifeTimeMinutes).toInstant());

    return JWT.create()
        .withSubject(subject)
        .withClaim(claimName, email)
        .withIssuedAt(new Date())
        .withIssuer(issuer)
        .withExpiresAt(expirationDate)
        .sign(Algorithm.HMAC256(secret));
  }

  public String generateTokenWithCommonUserTime(String email) {
    return generateToken(email, userTime);
  }
}
