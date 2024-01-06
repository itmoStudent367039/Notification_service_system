package ru.ifmo.userapi.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.repositories.NoticeRepository;

@Service
@RequiredArgsConstructor
public class NoticesService {
  private final NoticeRepository noticeRepository;

  @Transactional
  public synchronized void createNotice(Notice notice) {
    noticeRepository.save(notice);
  }

  @Transactional
  public synchronized void update(Notice notice) {
    createNotice(notice);
  }
}
