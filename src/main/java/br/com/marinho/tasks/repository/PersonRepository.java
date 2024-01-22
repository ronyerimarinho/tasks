package br.com.marinho.tasks.repository;

import br.com.marinho.tasks.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String email);

    Optional<Person> findByGuid(String guid);
}
