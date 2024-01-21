package ru.ifmo.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDTO implements Serializable {
  @NotEmpty(message="Telegram nickname should not be empty")
  @Size(min = 5, max = 30, message = "Telegram nickname size is between 5 and 30 symbols")
  private String telegramNickName;
}
