package ru.ifmo.noticesworker.send;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.models.Notice;

@RequiredArgsConstructor
public class RequestDirector {

  private final RestTemplate restTemplate;

  @Value("${application.urls.mail-service}")
  private String mailServiceUrl;

  void sendNoticeToMailService(Notice notice) throws RestClientException {
    Mail mail =
        Mail.builder()
            .to(notice.getPerson().getEmail())
            .message(notice.getValue())
            .subject("Hello!")
            .build();
    restTemplate.postForEntity(mailServiceUrl, new HttpEntity<>(mail), Void.class);
  }
}
