package ru.ifmo.backend.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.backend.user.Person;
import ru.ifmo.backend.user.PersonDetails;
import ru.ifmo.backend.user.PersonRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {
  private final PersonRepository personRepository;
  private static final String USER_NOT_FOUND_MSG = "User with email %s not found";

  @Autowired
  public PersonDetailsService(PersonRepository peopleRepository) {
    this.personRepository = peopleRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<Person> person =
        personRepository.findByEmail(email);

    if (person.isEmpty()) {
      throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email));
    }

    return new PersonDetails(person.get());
  }
}
