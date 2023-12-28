package ru.ifmo.userapi.services;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.userapi.models.Notice;

@RequiredArgsConstructor
public class NoticeSource implements ValueSource {
  private static final Logger log = LoggerFactory.getLogger(NoticeSource.class);
  private final AtomicLong nextValue = new AtomicLong(1);
  private final DataSender dataSender;

  @Override
  public void generate() {
    var executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(() -> dataSender.send(makeValue()), 0, 1, TimeUnit.SECONDS);
    log.info("generation started");
  }

  private Notice makeValue() {
    var id = nextValue.getAndIncrement();
    return new Notice(id, "stVal:" + id);
  }
}
