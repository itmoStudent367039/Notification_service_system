package ru.ifmo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ifmo.common.models.Person;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
  private Long id;

  private String value;

  private String subject;

  private Person person;
}
