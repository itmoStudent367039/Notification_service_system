package ru.ifmo.noticesworker.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.noticesworker.models.NoticeState;

@Component
@RequiredArgsConstructor
public class NoticeDAO {
  private static final String UPDATE_STATE = "UPDATE Notice SET state=? WHERE id=?";
  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public void updateNoticeState(Long id, NoticeState state) {
    jdbcTemplate.update(UPDATE_STATE, id, state);
  }
}
