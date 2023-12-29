package ru.ifmo.userapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ifmo.userapi.models.Notice;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final PeopleService peopleService;
  private final DataSender dataSender;

  public void notifyUsers(final String message) {
    peopleService.getPeople().parallelStream()
        .forEach(
            person -> {
              Notice notice = Notice.builder().person(person).value(message).build();
              dataSender.send(notice);
            });
  }
}
