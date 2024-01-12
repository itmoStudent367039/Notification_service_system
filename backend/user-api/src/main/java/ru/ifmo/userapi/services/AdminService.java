package ru.ifmo.userapi.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.ifmo.common.dto.NoticeDTO;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;
import ru.ifmo.common.responses.Message;
import ru.ifmo.userapi.util.ObjectConverter;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
  private final PeopleService peopleService;
  private final DataSender dataSender;
  private final NoticesService noticesService;
  private final ObjectConverter converter;

  public void notifyUsers(Message message) {
    String adminEmail = getAuthenticatedAdminEmail();
    noticesService.deleteAll();

    peopleService
        .getPeopleExceptOne(adminEmail)
        .forEach(
            person -> {
              Notice notice =
                  Notice.builder()
                      .person(person)
                      .value(message.getMessage())
                      .subject(message.getSubject())
                      .state(NoticeState.PREPARE)
                      .build();
              noticesService.createNotice(notice);
              dataSender.send(converter.convertToObject(notice, NoticeDTO.class), notice);
            });

//    List<Notice> notSent;
//    do {
//      notSent = noticesService.getNotSentNotices();
//      log.warn(String.format("Not sent notices: %s", notSent.stream().map(Notice::getId).toList()));
//      notSent.forEach(
//          notice -> dataSender.send(converter.convertToObject(notice, NoticeDTO.class), notice));
//    } while (!notSent.isEmpty());
    noticesService.deleteAll();
  }

  private String getAuthenticatedAdminEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (String) authentication.getPrincipal();
  }
}
