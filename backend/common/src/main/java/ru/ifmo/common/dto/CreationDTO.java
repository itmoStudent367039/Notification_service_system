package ru.ifmo.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreationDTO implements Serializable {
  @NotEmpty(message = "Username could not be empty")
  @Size(min = 2, max = 31, message = "Username size is between 2 and 31 symbols")
  private String username;

  @NotEmpty(message = "Email should not be empty")
  @Email(message = "Email should be valid")
  @Size(max = 63, message = "Email should be less or equals 63 symbols")
  private String email;
}
