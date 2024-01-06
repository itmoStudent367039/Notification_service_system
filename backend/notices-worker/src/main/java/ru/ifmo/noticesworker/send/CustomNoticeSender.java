package ru.ifmo.noticesworker.send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;

@RequiredArgsConstructor
@Slf4j
public class CustomNoticeSender implements NoticeSender {
  private final RequestDirector requestDirector;

  @Override
  public NoticeState send(Notice notice) {
    try {
      this.sendNoticeToMailService(notice);
      return NoticeState.DELIVERED;
    } catch (HttpClientErrorException e) {
      log.error(e.getStatusText() + " " + e.getStatusCode());
    } catch (HttpServerErrorException e) {
      log.error(e.getMessage() + " " + e.getStatusText());
    }
    return NoticeState.NOT_DELIVERED;
  }

  private void sendNoticeToMailService(Notice notice) throws RestClientException {
    requestDirector.sendNoticeToMailService(notice);
  }
}
