package ru.ifmo.userapi.services;

import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.common.models.Notice;

public interface DataSender {
  void send(NoticeDTO noticeDTO, Notice notice);
}
