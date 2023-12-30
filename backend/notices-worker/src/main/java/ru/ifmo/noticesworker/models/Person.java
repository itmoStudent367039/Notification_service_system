package ru.ifmo.noticesworker.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {
  private Integer id;
  private String username;
  private String email;
  private Long telegramChatId;
  private Integer vkId;
}
