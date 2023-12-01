package ru.ifmo.authapi.util.validators;

import ru.ifmo.authapi.util.exceptions.DomainNotExists;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DomainValidator {
  public void throwExceptionIfDomainNotExists(String email)
      throws UnknownHostException, DomainNotExists {
    String[] parts = email.split("@");

    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid email format");
    }

    String domain = parts[1];
    InetAddress inetAddress = InetAddress.getByName(domain);

    if (inetAddress.getHostAddress() == null) {
      throw new DomainNotExists(email);
    }
  }
}
