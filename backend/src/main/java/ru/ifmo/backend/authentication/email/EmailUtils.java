package ru.ifmo.backend.authentication.email;

public class EmailUtils {

  public static String getEmailMessage(String name, String host, String token) {
    return "Hello "
        + name
        + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n"
        + getVerificationUrl(host, token)
        + "\n\n® Notification Service System";
  }

  public static String getVerificationUrl(String host, String token) {
    return host + "/auth/confirm?token=" + token;
  }
}
