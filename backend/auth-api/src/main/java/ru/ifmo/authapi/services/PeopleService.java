package ru.ifmo.authapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.authapi.repositories.PersonRepository;
import ru.ifmo.authapi.user.Person;
import ru.ifmo.authapi.user.PersonRole;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
  private final PersonRepository peopleRepository;

  @Autowired
  public PeopleService(PersonRepository peopleRepository) {
    this.peopleRepository = peopleRepository;
  }

  public Optional<Person> findByEmail(String email) {
    return peopleRepository.findByEmail(email);
  }

  @Transactional
  public void save(Person person) {
    person.setRole(PersonRole.ROLE_USER);
    peopleRepository.save(person);
  }

  @Transactional
  public void update(Person person) {
    peopleRepository.save(person);
  }

  @Transactional
  public void delete(Person person) {
    peopleRepository.delete(person);
  }
}
