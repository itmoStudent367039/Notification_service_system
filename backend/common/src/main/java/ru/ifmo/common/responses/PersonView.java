package ru.ifmo.common.responses;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonView implements Serializable {
  private String username;

  private String email;

  private String telegramNickName;
  private String personRole;

//  private Integer vkId;
}
