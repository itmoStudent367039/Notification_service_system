package ru.ifmo.notificationservice.Notificationservice.models;

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
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotEmpty(message = "Username could not be empty")
    @Size(min = 2, max = 31, message = "Username size is between 2 and 31 symbols")
    private String username;

    @Column
    @NotEmpty(message = "Password could not be empty")
    private String password;

    @Column
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Size(max = 63, message = "Email should be less or equals 63 symbols")
    private String email;

    @Column
    private String role;

    @Column
    private Long telegramChatId;
    @Column
    private Integer vkId;

}
