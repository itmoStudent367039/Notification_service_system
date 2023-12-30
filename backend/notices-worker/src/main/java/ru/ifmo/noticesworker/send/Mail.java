package ru.ifmo.noticesworker.send;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class Mail implements Serializable {

  private String to;

  private String message;

  private String subject;
}
