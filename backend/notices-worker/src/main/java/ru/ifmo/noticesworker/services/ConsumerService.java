package ru.ifmo.noticesworker.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;
import ru.ifmo.common.repositories.NoticeRepository;
import ru.ifmo.noticesworker.send.NoticeSender;

@Service
@RequiredArgsConstructor
public class ConsumerService {
  private final NoticeRepository noticeRepository;
  private final NoticeSender noticeSender;

  @KafkaListener(
      topics = "${application.kafka.topic.name}",
      containerFactory = "kafkaListenerContainerFactory")
  public void listen(Notice notice) {
    System.out.println(Thread.currentThread().getName());
    NoticeState state = noticeSender.send(notice);
    notice.setState(state);
    noticeRepository.save(notice);
  }
}
