package ru.ifmo.Notification_service_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.Notification_service_system.models.Person;

import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin(String login);
}
