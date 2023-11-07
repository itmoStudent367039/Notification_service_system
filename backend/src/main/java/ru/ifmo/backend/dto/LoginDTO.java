package ru.ifmo.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {
  @NotEmpty(message = "Username could not be empty")
  @Size(min = 2, max = 31, message = "Username size is between 2 and 31 symbols")
  private String username;

  @NotEmpty(message = "Password could not be empty")
  @Size(min = 2, max = 31, message = "Password size is between 2 and 31 symbols")
  private String password;
}
