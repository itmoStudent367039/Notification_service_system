package ru.ifmo.noticesworker.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Notice {
  private Long id;
  private String value;
  private Person person;
  private NoticeState state;
}
