package ru.ifmo.userapi.services;

import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import ru.ifmo.userapi.models.Notice;

@Slf4j
public class DataSenderKafka implements DataSender {
  private final KafkaTemplate<String, Notice> template;

  private final Consumer<Notice> sendAsk;

  private final String topic;

  public DataSenderKafka(
      String topic, KafkaTemplate<String, Notice> template, Consumer<Notice> sendAsk) {
    this.topic = topic;
    this.template = template;
    this.sendAsk = sendAsk;
  }

  @Override
  public void send(Notice notice) {
    try {
      log.info("value:{}", notice);
      // TODO: add notice to database
      template
          .send(topic, notice)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  // TODO: mark as delivered to worker-service
                  log.info(
                      "message id:{} was sent, offset:{}",
                      notice.getId(),
                      result.getRecordMetadata().offset());
                  sendAsk.accept(notice);
                } else {
                  // TODO: mark as not delivered to worker-service
                  log.error("message id:{} was not sent", notice.getId(), ex);
                }
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", notice, ex);
    }
  }
}
