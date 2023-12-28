package ru.ifmo.userapi.services;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import ru.ifmo.userapi.models.Notice;

public class DataSenderKafka implements DataSender {
  private static final Logger log = LoggerFactory.getLogger(DataSenderKafka.class);

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
      template
          .send(topic, notice)
          .whenComplete(
              (result, ex) -> {
                if (ex == null) {
                  log.info(
                      "message id:{} was sent, offset:{}",
                      notice.getId(),
                      result.getRecordMetadata().offset());
                  sendAsk.accept(notice);
                } else {
                  log.error("message id:{} was not sent", notice.getId(), ex);
                }
              });
    } catch (Exception ex) {
      log.error("send error, value:{}", notice, ex);
    }
  }
}
