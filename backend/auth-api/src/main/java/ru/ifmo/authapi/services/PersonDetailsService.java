package ru.ifmo.authapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.authapi.repositories.PersonRepository;
import ru.ifmo.authapi.user.Person;
import ru.ifmo.authapi.user.PersonDetails;

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
