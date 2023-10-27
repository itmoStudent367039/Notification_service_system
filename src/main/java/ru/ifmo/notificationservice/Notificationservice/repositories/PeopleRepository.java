package ru.ifmo.notificationservice.Notificationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.notificationservice.Notificationservice.models.Person;

import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByEmail(String email);
}
