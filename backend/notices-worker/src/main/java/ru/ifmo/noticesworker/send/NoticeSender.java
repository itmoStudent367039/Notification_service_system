package ru.ifmo.noticesworker.send;

import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.common.models.NoticeState;

public interface NoticeSender {
  NoticeState send(NoticeDTO notice);
}
