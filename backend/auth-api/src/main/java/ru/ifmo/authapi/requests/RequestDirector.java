package ru.ifmo.authapi.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.ifmo.authapi.dto.RegistrationDTO;
import ru.ifmo.authapi.responses.PersonView;


@Component
public class RequestDirector {
  @Value("${urls.userApiCreatePerson}")
  private String userApiCreatePersonUrl;

  @Value("${urls.userApiGetPerson}")
  private String userApiGetPersonUrl;

  private final RestTemplate restTemplate;

  @Autowired
  public RequestDirector(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void sendUserApiCreationRequest(RegistrationDTO dto, String token)
      throws RestClientException {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<RegistrationDTO> request = new HttpEntity<>(dto, headers);
    System.out.println("auth: point 1 - try to send token:" + token);

    restTemplate.postForEntity(userApiCreatePersonUrl, request, Void.class);
  }

  public ResponseEntity<PersonView> getCurrentPerson(String token) throws RestClientException {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Void> request = new HttpEntity<>(headers);
    System.out.println("auth: point 1 - try to get current user");

    return restTemplate.exchange(userApiGetPersonUrl, HttpMethod.GET, request, PersonView.class);
  }
}
