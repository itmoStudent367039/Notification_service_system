package ru.ifmo.notificationservice.Notificationservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "person")
@NoArgsConstructor
@Data
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    @NotEmpty(message = "Username could not be empty")
    @Size(min = 2, max = 31, message = "Username size is between 2 and 31 symbols")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password could not be empty")
    private String password;

    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Size(max = 63, message = "Email should be less or equals 63 symbols")
    private String email;

    @Column(name = "role")
    private String role;

}
