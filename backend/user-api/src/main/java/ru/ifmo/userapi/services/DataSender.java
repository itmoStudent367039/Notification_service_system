package ru.ifmo.userapi.services;

import ru.ifmo.userapi.models.Notice;

public interface DataSender {
  void send(Notice notice);
}
