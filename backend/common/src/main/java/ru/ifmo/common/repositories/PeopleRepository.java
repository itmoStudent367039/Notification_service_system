package ru.ifmo.common.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ifmo.common.models.Person;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
  Optional<Person> findByEmail(String email);

  Optional<Person> findByUsername(String username);
}
