package ru.ifmo.noticesworker.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.common.models.Notice;
import ru.ifmo.common.repositories.NoticeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticesService {
  private final NoticeRepository noticeRepository;

  @Transactional
  public void update(Notice notice) {
    noticeRepository.save(notice);
  }

  public Optional<Notice> getNotice(Long id) {
    return noticeRepository.findById(id);
  }
}
