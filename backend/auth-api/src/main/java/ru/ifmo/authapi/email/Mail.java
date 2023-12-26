package ru.ifmo.authapi.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
public class Mail implements Serializable {

  @NotEmpty(message = "Email should not be empty")
  @Email(message = "Email should be valid")
  private String to;

  @NotEmpty(message = "Message should not be empty")
  private String message;

  @NotEmpty(message = "Subject should not be empty")
  private String subject;
}
