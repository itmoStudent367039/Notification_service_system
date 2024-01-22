package ru.ifmo.authapi.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.common.dto.CreationDTO;
import ru.ifmo.common.dto.RegistrationDTO;
import ru.ifmo.common.mail.Mail;
import ru.ifmo.common.responses.PersonView;

@Component
public class RequestDirector {
  private final RestTemplate restTemplate;

  @Value("${urls.userApiCreatePerson}")
  private String userApiCreatePersonUrl;

  @Value("${urls.userApiGetPerson}")
  private String userApiGetPersonUrl;

  @Value("${urls.mailServiceUrl}")
  private String mailServiceUrl;

  @Value("${urls.userApiDeletePerson}")
  private String userApiDeleteUrl;

  @Autowired
  public RequestDirector(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void sendUserApiCreationRequest(CreationDTO dto, String token)
      throws RestClientException {
    HttpHeaders headers = this.initHeadersWithAuthorization(token);
    HttpEntity<CreationDTO> request = new HttpEntity<>(dto, headers);
    System.out.println("auth: point 1 - try to send token:" + token);
    restTemplate.postForEntity(userApiCreatePersonUrl, request, Void.class);
  }

  public ResponseEntity<PersonView> getCurrentPerson(String token) throws RestClientException {
    HttpHeaders headers = this.initHeadersWithAuthorization(token);
    HttpEntity<Void> request = new HttpEntity<>(headers);
    System.out.println("auth: point 1 - try to get current user");
    return restTemplate.exchange(userApiGetPersonUrl, HttpMethod.GET, request, PersonView.class);
  }

  public void sendMessageToMailService(Mail mail) throws RestClientException {
    HttpEntity<Mail> request = new HttpEntity<>(mail);
    restTemplate.postForEntity(mailServiceUrl, request, Void.class);
  }

  public void sendUserApiDeleteRequest(String token) throws RestClientException {
    HttpHeaders headers = this.initHeadersWithAuthorization(token);
    HttpEntity<Void> request = new HttpEntity<>(headers);
    System.out.println("auth: point 1 - try to delete user");
    restTemplate.exchange(userApiDeleteUrl, HttpMethod.DELETE, request, Void.class);
  }

  private HttpHeaders initHeadersWithAuthorization(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
