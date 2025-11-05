package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PersonDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonMapper {
    private final PersonRepository personRepository;

    public PersonMapper(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person PersonDTOToPerson(PersonDTO personDTO) {
        if (personDTO == null) return null;
        if (personDTO.getId() != null) {
            return personRepository.findById(personDTO.getId()).orElse(null);
        } else {
            Person person = new Person();
            personRepository.save(person);
            return person;
        }
    }

    public Set<Person> PersonDTOsToPersons(Set<PersonDTO> personDTOs) {
        if (personDTOs.isEmpty()) return Collections.emptySet();
        return personDTOs.stream()
                .map(this::PersonDTOToPerson)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public PersonDTO PersonToPersonDTO(Person person) {
        if (person == null) return null;
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setFullName(person.getFullName());
        return  personDTO;
    }

    public Set<PersonDTO> PersonsToPersonDTOs(Set<Person> persons) {
        if (persons.isEmpty()) return Collections.emptySet();
        return persons.stream()
                .map(this::PersonToPersonDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
