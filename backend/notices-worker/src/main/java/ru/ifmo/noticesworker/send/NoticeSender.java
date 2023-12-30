package ru.ifmo.noticesworker.send;

import ru.ifmo.noticesworker.models.Notice;
import ru.ifmo.noticesworker.models.NoticeState;

public interface NoticeSender {
  NoticeState send(Notice notice);
}
