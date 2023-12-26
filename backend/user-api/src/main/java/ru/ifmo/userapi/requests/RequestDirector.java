package ru.ifmo.userapi.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RequestDirector {
  private final RestTemplate restTemplate;

  @Value("${urls.authApiAuthenticate}")
  private String authUrl;

  @Value("${urls.authApiDeleteUser}")
  private String deleteUrl;

  @Autowired
  public RequestDirector(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ResponseEntity<UserInfo> sendAuthApiAuthenticateRequest(String token)
      throws RestClientException {
    HttpHeaders headers = this.initHeadersWithAuthorization(token);
    return restTemplate.exchange(
        authUrl, HttpMethod.GET, new HttpEntity<>(headers), UserInfo.class);
  }

  public void sendAuthApiDeleteRequest(String token) throws RestClientException {
    HttpHeaders headers = this.initHeadersWithAuthorization(token);
    restTemplate.exchange(deleteUrl, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
  }

  private HttpHeaders initHeadersWithAuthorization(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
