package ru.ifmo.userapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.models.NoticeState;
import ru.ifmo.common.repositories.NoticeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticesService {
  private final NoticeRepository noticeRepository;

  @Transactional
  public void createNotice(Notice notice) {
    noticeRepository.save(notice);
  }

  @Transactional
  public void update(Notice notice) {
    createNotice(notice);
  }

  public List<Notice> getNotSentNotices() {
    return noticeRepository.findAll().stream()
        .filter(notice -> notice.getState().equals(NoticeState.SENDER_NOT_RECEIVED))
        .toList();
  }
  @Transactional
  public void deleteAll() {
    noticeRepository.deleteAll();
  }

  public Optional<Notice> getNotice(Long id) {
    return noticeRepository.findById(id);
  }
}
