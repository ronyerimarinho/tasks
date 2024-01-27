package br.com.marinho.tasks.service;

import br.com.marinho.tasks.exceptions.UserNotFoundException;
import br.com.marinho.tasks.model.Person;
import br.com.marinho.tasks.model.transport.operations.create.CreatePersonForm;
import br.com.marinho.tasks.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

    @Test
    @DisplayName("Get user in session by email when user is found")
    void loadByUsernameWhenUserIsFound() {
        String email = "marinho@gmail.com";
        Person person = new Person();
        person.setEmail(email);
        person.setPassword(this.passwordEncoder.encode("UmaSenhaForte"));
        BDDMockito.given(this.personRepository.findByEmail(email)).willReturn(Optional.of(person));

        Assertions.assertDoesNotThrow(() -> this.personService.loadUserByUsername(email));
    }

    @Test
    @DisplayName("Get user in session by email when user is not found")
    void loadByUsernameWhenUserIsNotFound() {
        String email = "marinho@gmail.com";
        BDDMockito.given(this.personRepository.findByEmail(email)).willReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> this.personService.loadUserByUsername(email));
    }

    @Test
    @DisplayName("Get user by email when user is found")
    void findByEmailWhenUserIsFound() {
        String email = "marinho@gmail.com";
        Person person = new Person();
        person.setEmail(email);
        person.setPassword(this.passwordEncoder.encode("UmaSenhaForte"));
        BDDMockito.given(this.personRepository.findByEmail(email)).willReturn(Optional.of(person));

        Assertions.assertDoesNotThrow(() -> this.personService.findByEmail(email));
    }

    @Test
    @DisplayName("Get user by email when user is not found")
    void findByEmailWhenUserIsNotFound() {
        String email = "marinho@gmail.com";
        BDDMockito.given(this.personRepository.findByEmail(email)).willReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> this.personService.findByEmail(email));
    }

    @Test
    @DisplayName("Get user by GUID when user is found")
    void findByGUIDWhenUserIsFound() {
        String guid = UUID.randomUUID().toString();
        Person person = new Person();
        person.setGuid(guid);
        person.setPassword(this.passwordEncoder.encode("UmaSenhaForte"));

        BDDMockito.given(this.personRepository.findByGuid(guid)).willReturn(Optional.of(person));
        Assertions.assertDoesNotThrow(() -> this.personService.findByGuid(guid));
    }

    @Test
    @DisplayName("Get user by GUID when user is not found")
    void getByGUIDWhenIsNotFound() {
        String guid = UUID.randomUUID().toString();
        BDDMockito.given(this.personRepository.findByGuid(guid)).willReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> this.personService.findByGuid(guid));
    }

    @Test
    @DisplayName("Get user as object by GUID when user is found")
    void getAsObjectByGUIDWhenIsFound() {
        String guid = UUID.randomUUID().toString();
        Person person = new Person();
        person.setGuid(guid);
        person.setPassword(this.passwordEncoder.encode("UmaSenhaForte"));
        BDDMockito.given(this.personRepository.findByGuid(guid)).willReturn(Optional.of(person));

        Assertions.assertDoesNotThrow(() -> this.personService.getByGuid(guid));
    }

    @Test
    @DisplayName("Get user as object by GUID when user is not found")
    void getAsObjectByGUIDWhenIsNotFound() {
        String guid = UUID.randomUUID().toString();
        BDDMockito.given(this.personRepository.findByGuid(guid)).willReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> this.personService.getByGuid(guid));
    }

    @Test
    @DisplayName("Create user on database")
    void createUserOnDatabaseWithNoFail() {
        /* Arrange */
        CreatePersonForm form =
                new CreatePersonForm("Ronyeri Marinho", "marinho@gmail.com", "UmaSenhaForte");

        /* Act */
        this.personService.create(form);

        /* Assert */
        BDDMockito.then(this.personRepository).should().save(this.personCaptor.capture());
        Person createdPerson = this.personCaptor.getValue();

        Assertions.assertEquals(form.name(), createdPerson.getName());
        Assertions.assertEquals(form.email(), createdPerson.getEmail());
        Assertions.assertEquals(this.passwordEncoder.encode(form.password()), createdPerson.getPassword());
    }
}