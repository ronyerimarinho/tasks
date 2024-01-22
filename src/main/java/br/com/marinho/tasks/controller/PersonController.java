package br.com.marinho.tasks.controller;

import br.com.marinho.tasks.exceptions.UserNotFoundException;
import br.com.marinho.tasks.model.transport.PersonDTO;
import br.com.marinho.tasks.model.transport.operations.create.CreatePersonForm;
import br.com.marinho.tasks.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> get(@PathVariable("id") String guid) throws UserNotFoundException {
        PersonDTO response = this.personService.getByGuid(guid);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid CreatePersonForm form) {
        PersonDTO response = this.personService.create(form);
        return ResponseEntity.created(URI.create(String.format("/person/%s", response.guid()))).body(response);
    }
}
