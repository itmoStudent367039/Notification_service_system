package ru.ifmo.userapi.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestDirector {
  @Value("${urls.authApiAuthenticate}")
  private String authUrl;

  private final RestTemplate restTemplate;

  @Autowired
  public RequestDirector(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ResponseEntity<UserInfo> sendAuthApiAuthenticateRequest(String token)
      throws RestClientException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    HttpEntity<String> request = new HttpEntity<>(headers);

    return restTemplate.exchange(authUrl, HttpMethod.GET, request, UserInfo.class);
  }
}
