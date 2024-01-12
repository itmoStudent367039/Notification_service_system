package ru.ifmo.common.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@DynamicUpdate
@Entity
@Table
public class Notice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column(nullable = false)
  private String value;

  @Column(nullable = false)
  private String subject;

  @OneToOne
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person person;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NoticeState state;

}
