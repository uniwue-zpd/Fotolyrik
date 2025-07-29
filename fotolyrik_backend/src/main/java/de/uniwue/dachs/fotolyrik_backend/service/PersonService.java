package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final FileRepository fileRepository;

    public PersonService(PersonRepository personRepository, FileRepository fileRepository) {
        this.personRepository = personRepository;
        this.fileRepository = fileRepository;
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
        person.setImage(person.getImage() != null
                ? getImage(person.getImage().getId())
                : null);
        return personRepository.save(person);
    }

    // PUT update existing person
    @Transactional
    public Person updatePerson(Long id, Person updatedPerson) {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    existingPerson.setFirstName(updatedPerson.getFirstName());
                    existingPerson.setLastName(updatedPerson.getLastName());
                    existingPerson.setBirthYear(updatedPerson.getBirthYear());
                    existingPerson.setDeathYear(updatedPerson.getDeathYear());
                    existingPerson.setPseudonyms(updatedPerson.getPseudonyms());
                    existingPerson.setSex(updatedPerson.getSex());
                    existingPerson.setGndId(updatedPerson.getGndId());
                    existingPerson.setImage(updatedPerson.getImage() != null
                            ? getImage(updatedPerson.getImage().getId())
                            : null);
                    return personRepository.save(existingPerson);
                })
                .orElse(null);
    }

    // DELETE person by ID
    @Transactional
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException("Person with id '" + id + "' does not exist");
        }
        personRepository.deleteById(id);
    }

    private File getImage(Long id) {
        if (id == null) {
            throw  new IllegalArgumentException();
        }
        return fileRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Image with id '" + id + "' does not exist")
        );
    }
}
