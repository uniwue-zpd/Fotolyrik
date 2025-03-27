package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public Iterable<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Person addPerson(@RequestBody Person person) {
        return this.personRepository.save(person);
    }

    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person person) {
        Person existingPerson = personRepository.findById(id).orElse(null);
        if (existingPerson == null) {
            return null;
        }
        existingPerson.setFirstName(person.getFirstName());
        existingPerson.setLastName(person.getLastName());
        existingPerson.setBirthYear(person.getBirthYear());
        existingPerson.setDeathYear(person.getDeathYear());
        existingPerson.setPseudonym(person.getPseudonym());
        existingPerson.setSex(person.getSex());

        return personRepository.save(existingPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return ResponseEntity.ok("Person with id '" + id + "' deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
