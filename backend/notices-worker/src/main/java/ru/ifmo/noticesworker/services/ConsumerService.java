package ru.ifmo.noticesworker.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;
import ru.ifmo.noticesworker.send.NoticeSender;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {
  private final NoticesService noticesService;
  private final NoticeSender noticeSender;

  @KafkaListener(
      topics = "${application.kafka.topic.name}",
      containerFactory = "kafkaListenerContainerFactory")
  public void listen(NoticeDTO noticeDTO) {
    if (noticeDTO.getPerson() == null) log.info("Current person is null");
    NoticeState state = noticeSender.send(noticeDTO);
    Optional<Notice> optional = noticesService.getNotice(noticeDTO.getId());
    if (optional.isPresent()) {
      Notice notice = optional.get();
      notice.setState(state);
      noticesService.update(notice);
    }
  }
}
