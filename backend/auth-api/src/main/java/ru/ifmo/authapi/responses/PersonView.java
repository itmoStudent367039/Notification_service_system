package ru.ifmo.authapi.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonView implements Serializable {
  private String username;

  private String email;

  private Long telegramChatId;

  private Integer vkId;
}
