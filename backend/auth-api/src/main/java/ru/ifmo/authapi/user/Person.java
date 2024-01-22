package ru.ifmo.authapi.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "person")
@NoArgsConstructor
@DynamicUpdate
@Data
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer id;

  @NotEmpty(message = "Password could not be empty")
  @Column(nullable = false)
  private String password;

  @NotEmpty(message = "Email should not be empty")
  @Email(message = "Email should be valid")
  @Size(max = 63, message = "Email should be less or equals 63 symbols")
  @Column(nullable = false, unique = true)
  private String email;

  @NotEmpty(message = "Username could not be empty")
  @Size(min = 2, max = 31, message = "Username size is between 2 and 31 symbols")
  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PersonRole role;

  @Column(nullable = false)
  private Boolean locked = false;

  @Column(nullable = false)
  private Boolean enable = false;

}
