package ru.ifmo.common.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.common.models.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {}
