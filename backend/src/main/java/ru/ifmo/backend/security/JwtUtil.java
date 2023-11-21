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

  @Value("${jwt.jwtSecret}")
  private String secret;

  @Value("${jwt.expirationConfirmTime}")
  private long confirmTime;

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

  public String generateTokenWithConfirmExpirationTime(String email) {
    return generateToken(email, confirmTime);
  }

  public String generateTokenWithCommonUserTime(String email) {
    return generateToken(email, userTime);
  }

  public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
    JWTVerifier verifier =
        JWT.require(Algorithm.HMAC256(secret)).withSubject(subject).withIssuer(issuer).build();

    DecodedJWT jwt = verifier.verify(token);
    return jwt.getClaim(claimName).asString();
  }
}
