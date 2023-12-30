package ru.ifmo.userapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ifmo.userapi.models.Notice;
import ru.ifmo.userapi.models.NoticeState;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final PeopleService peopleService;
  private final DataSender dataSender;
  private final NoticesService noticesService;

  public void notifyUsers(final String message) {
    String adminEmail = getAuthenticatedAdminEmail();
    peopleService.getPeopleExceptOne(adminEmail).parallelStream()
        .forEach(
            person -> {
              Notice notice =
                  Notice.builder()
                      .person(person)
                      .value(message)
                      .person(person)
                      .state(NoticeState.PREPARE)
                      .build();
              noticesService.createNotice(notice);
              dataSender.send(notice);
            });
  }

  private String getAuthenticatedAdminEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (String) authentication.getPrincipal();
  }
}
