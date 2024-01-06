package ru.ifmo.noticesworker.send;

import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;

public interface NoticeSender {
  NoticeState send(Notice notice);
}
