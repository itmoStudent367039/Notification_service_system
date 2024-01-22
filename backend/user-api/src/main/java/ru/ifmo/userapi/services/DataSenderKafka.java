package ru.ifmo.userapi.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;


@Slf4j
@RequiredArgsConstructor
public class DataSenderKafka implements DataSender {
  private final KafkaTemplate<String, NoticeDTO> template;

  private final NoticesService noticesService;

  private final String topic;

  @Override
  public void send(NoticeDTO noticeDTO, Notice notice) {
    try {
      log.info("value:{}", notice.getId());
      log.info("person:{}", notice.getPerson().getEmail());
      template
          .send(topic, noticeDTO)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  notice.setState(NoticeState.SENDER_RECEIVED);
                  log.info(
                      "message id:{} was sent, offset:{}",
                      notice.getPerson().getEmail(),
                      result.getRecordMetadata().offset());
                } else {
                  notice.setState(NoticeState.SENDER_NOT_RECEIVED);
                  log.error("message id:{} was not sent", notice.getPerson().getEmail(), ex);
                }
                noticesService.update(notice);
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", notice.getId(), ex);
    }
  }
}
