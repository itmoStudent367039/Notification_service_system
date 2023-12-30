package ru.ifmo.noticesworker.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ifmo.noticesworker.dao.NoticeDAO;
import ru.ifmo.noticesworker.models.Notice;
import ru.ifmo.noticesworker.models.NoticeState;
import ru.ifmo.noticesworker.send.NoticeSender;

@Service
@RequiredArgsConstructor
public class ConsumerService {
  private final NoticeDAO noticeDAO;

  private final NoticeSender noticeSender;

  @KafkaListener(
      topics = "${application.kafka.topic.name}",
      containerFactory = "kafkaListenerContainerFactory")
  public void listen(Notice notice) {
    System.out.println(Thread.currentThread().getName());
    NoticeState state = noticeSender.send(notice);
    noticeDAO.updateNoticeState(notice.getId(), state);
  }
}
