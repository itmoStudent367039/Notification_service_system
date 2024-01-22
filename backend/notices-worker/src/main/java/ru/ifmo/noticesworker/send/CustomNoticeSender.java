package ru.ifmo.noticesworker.send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.common.models.NoticeState;

@RequiredArgsConstructor
@Slf4j
public class CustomNoticeSender implements NoticeSender {
  private final RequestDirector requestDirector;

  @Override
  public NoticeState send(NoticeDTO noticeDTO) {
    try {
      this.sendNoticeToMailService(noticeDTO);
      this.sendNoticeToTelegramBot(noticeDTO);
      return NoticeState.DELIVERED;
    } catch (HttpClientErrorException e) {
      log.error(e.getStatusText() + " " + e.getStatusCode());
    } catch (HttpServerErrorException e) {
      log.error(e.getMessage() + " " + e.getStatusText());
    }
    return NoticeState.NOT_DELIVERED;
  }

  private void sendNoticeToMailService(NoticeDTO noticeDTO) throws RestClientException {
    requestDirector.sendNoticeToMailService(noticeDTO);
  }
  private void sendNoticeToTelegramBot(NoticeDTO noticeDTO) throws RestClientException {
    requestDirector.sendNoticeToTelegramBot(noticeDTO);
  }
}
