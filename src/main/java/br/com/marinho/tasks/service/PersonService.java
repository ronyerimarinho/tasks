package br.com.marinho.tasks.service;

import br.com.marinho.tasks.exceptions.UserNotFoundException;
import br.com.marinho.tasks.model.Person;
import br.com.marinho.tasks.model.transport.PersonDTO;
import br.com.marinho.tasks.model.transport.operations.create.CreatePersonForm;
import br.com.marinho.tasks.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.personRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User by email not found: %s", username)));
    }

    public Person findByEmail(String email) throws UserNotFoundException {
        return this.personRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User by email not found: %s", email)));
    }

    public Person findByGuid(String guid) throws UserNotFoundException {
        return this.personRepository.findByGuid(guid)
                .orElseThrow(() -> new UserNotFoundException(String.format("User by GUID not found: %s", guid)));
    }

    public PersonDTO getByGuid(String guid) throws UserNotFoundException {
        return this.personRepository.findByGuid(guid).map(PersonDTO::new)
                .orElseThrow(() -> new UserNotFoundException(String.format("user not found from GUID: %s", guid)));
    }

    @Transactional
    public PersonDTO create(CreatePersonForm form) {
        LOGGER.info("Starting user creation...");
        String password = this.passwordEncoder.encode(form.password());
        Person person = new Person(form, password);
        this.personRepository.save(person);
        return new PersonDTO(person);
    }
}
