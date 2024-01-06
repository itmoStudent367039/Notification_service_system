package ru.ifmo.userapi.services;

import ru.ifmo.common.models.Notice;

public interface DataSender {
  void send(Notice notice);
}
