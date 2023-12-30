package ru.ifmo.userapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.userapi.models.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {}
