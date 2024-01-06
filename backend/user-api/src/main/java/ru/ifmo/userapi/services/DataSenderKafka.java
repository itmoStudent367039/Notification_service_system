package ru.ifmo.userapi.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;

@Slf4j
@RequiredArgsConstructor
public class DataSenderKafka implements DataSender {
  private final KafkaTemplate<String, Notice> template;

  private final NoticesService noticesService;

  private final String topic;

  @Override
  public void send(Notice notice) {
    try {
      log.info("value:{}", notice);
      template
          .send(topic, notice)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  // TODO: mark as delivered to worker-service
                  notice.setState(NoticeState.SENDER_RECEIVED);
                  log.info(
                      "message id:{} was sent, offset:{}",
                      notice.getPerson().getEmail(),
                      result.getRecordMetadata().offset());
                } else {
                  // TODO: mark as not delivered to worker-service
                  notice.setState(NoticeState.SENDER_NOT_RECEIVED);
                  log.error("message id:{} was not sent", notice.getPerson().getEmail(), ex);
                }
                noticesService.update(notice);
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", notice, ex);
    }
  }
}
