package ru.ifmo.notificationservice.Notificationservice.dto;

import lombok.Data;

@Data
public class PersonView {

    private int id;

    private String username;

    private String email;

    private final long timestamp = System.currentTimeMillis();

}
