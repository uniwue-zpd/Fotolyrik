package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // GET all persons
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // GET person by ID
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    // POST create new person
    @Transactional
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    // PUT update existing person
    @Transactional
    public Person updatePerson(Long id, Person updatedPerson) {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    existingPerson.setFirst_name(updatedPerson.getFirst_name());
                    existingPerson.setLast_name(updatedPerson.getLast_name());
                    existingPerson.setBirth_year(updatedPerson.getBirth_year());
                    existingPerson.setDeath_year(updatedPerson.getDeath_year());
                    existingPerson.setPseudonym(updatedPerson.getPseudonym());
                    existingPerson.setSex(updatedPerson.getSex());
                    existingPerson.setGnd_id(updatedPerson.getGnd_id());
                    return personRepository.save(existingPerson);
                })
                .orElse(null);
    }

    // DELETE person by ID
    @Transactional
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new IllegalArgumentException("Person with id '" + id + "' does not exist");
        }
        personRepository.deleteById(id);
    }
}
