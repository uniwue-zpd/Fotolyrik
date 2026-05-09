package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
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

    /**
     * GET all persons sorted by first name and last name
     * @return {@link List} of {@link Person}
     */
    public List<Person> getAllPersons() {
        return personRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName", "lastName"));
    }

    /**
     * GET person by ID
     * @param id of the person
     * @return {@link Optional} of {@link Person}
     */
    public Optional<Person> getPersonById(Long id) {
        return personRepository.findById(id);
    }

    /**
     * POST create a new person
     * @param person {@link Person} to create
     * @return created {@link Person}
     */
    @Transactional
    public Person createPerson(Person person) {
        person.setImage(person.getImage() != null
                ? getImage(person.getImage().getId())
                : null);
        return personRepository.save(person);
    }

    /**
     * PUT update an existing person
     * @param id of the person to update
     * @param updatedPerson with updated values
     * @return updated {@link Person}
     */
    @Transactional
    public Person updatePerson(Long id, Person updatedPerson) {
        return personRepository.findById(id)
                .map(existingPerson -> {
                    existingPerson.updateBaseEntityNotes(updatedPerson);
                    existingPerson.setFirstName(updatedPerson.getFirstName());
                    existingPerson.setLastName(updatedPerson.getLastName());
                    existingPerson.setStudioName(updatedPerson.getStudioName());
                    existingPerson.setBirthYear(updatedPerson.getBirthYear());
                    existingPerson.setDeathYear(updatedPerson.getDeathYear());
                    existingPerson.setPseudonyms(updatedPerson.getPseudonyms());
                    existingPerson.setSex(updatedPerson.getSex());
                    existingPerson.setGndId(updatedPerson.getGndId());
                    existingPerson.setNotes(updatedPerson.getNotes());
                    existingPerson.setImage(updatedPerson.getImage() != null
                            ? getImage(updatedPerson.getImage().getId())
                            : null);
                    return personRepository.save(existingPerson);
                })
                .orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * DELETE person by ID
     * @param id of the person to delete
     */
    @Transactional
    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new EntityNotFoundException("Person with id '" + id + "' does not exist");
        }
        personRepository.deleteById(id);
    }

    /**
     * Helper method to get an image by ID
     * @param id of the image
     * @return {@link File} with the given ID
     */
    private File getImage(Long id) {
        if (id == null) {
            throw  new IllegalArgumentException();
        }
        return fileRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Image with id '" + id + "' does not exist")
        );
    }
}
