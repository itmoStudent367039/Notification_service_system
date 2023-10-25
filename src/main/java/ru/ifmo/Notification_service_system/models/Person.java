package ru.ifmo.Notification_service_system.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persons")
@NoArgsConstructor
@Getter
@Setter
public class Person {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotEmpty(message = "Login could not be empty")
    @Size(min = 2, max = 31, message = "Login size is between 2 and 31 symbols")
    private String login;

    @Column
    @NotEmpty(message = "Password could not be empty")
    @Size(min = 2, max = 31, message = "Login size is between 2 and 31 symbols")
    private String password;

    @Column
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Size(max = 63, message = "Email should be less or equals 63 symbols")
    private String email;

    @Column
    private String role;
}
