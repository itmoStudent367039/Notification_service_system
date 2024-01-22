package ru.ifmo.common.responses;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Message {
  @NotEmpty(message = "Message could not be empty")
  private String message;

  @NotEmpty(message = "Subject could not be empty")
  private String subject;
}
