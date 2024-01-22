package ru.ifmo.noticesworker.send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramMessage implements Serializable {
    private String nickName;
    private String message;
}
