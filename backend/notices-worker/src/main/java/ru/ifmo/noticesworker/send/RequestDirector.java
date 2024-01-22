package ru.ifmo.noticesworker.send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.common.mail.Mail;
import ru.ifmo.common.models.Person;

@RequiredArgsConstructor
@Slf4j
public class RequestDirector {

  private final RestTemplate restTemplate;

  @Value("${application.urls.mail-service}")
  private String mailServiceUrl;

  @Value("${application.urls.telegram-bot}")
  private String telegramBotUrl;

  void sendNoticeToMailService(NoticeDTO notice) throws RestClientException {
    Person person = notice.getPerson();
    if (person != null) {
      Mail mail =
          Mail.builder()
              .to(person.getEmail())
              .message(notice.getValue())
              .subject(notice.getSubject())
              .build();
      restTemplate.postForEntity(mailServiceUrl, new HttpEntity<>(mail), Void.class);
    } else {
      log.error("Person is null for the given notice: {}", notice);
    }
  }

  void sendNoticeToTelegramBot(NoticeDTO noticeDTO) throws RestClientException {
    Person person = noticeDTO.getPerson();
    if (person != null) {
      TelegramMessage telegramMessage =
          new TelegramMessage(person.getTelegramNickName(), noticeDTO.getValue());
      restTemplate.postForEntity(telegramBotUrl, new HttpEntity<>(telegramMessage), Void.class);
    } else {
      log.error("Person is null for the given notice: {}", noticeDTO);
    }
  }
}
