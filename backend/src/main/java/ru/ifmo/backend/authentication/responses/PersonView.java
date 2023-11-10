package ru.ifmo.backend.authentication.responses;

import lombok.Data;

@Data
public class PersonView {

  private String username;

  private String email;

  private String role;

  private Long telegramChatId;

  private Integer vkId;

}
