package ru.ifmo.notificationservice.Notificationservice.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.ifmo.notificationservice.Notificationservice.dto.PersonView;

@Component
public class ResponseConstructor {
    public ResponseEntity<PersonView> makeOkResponse(PersonView view, String token) {
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        token
                )
                .body(view);
    }
}
