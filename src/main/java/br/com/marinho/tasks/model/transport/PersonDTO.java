package br.com.marinho.tasks.model.transport;

import br.com.marinho.tasks.model.Person;

public record PersonDTO(String guid, String name, String email) {

    public PersonDTO(Person person) {
        this(person.getGuid(), person.getName(), person.getEmail());
    }
}
