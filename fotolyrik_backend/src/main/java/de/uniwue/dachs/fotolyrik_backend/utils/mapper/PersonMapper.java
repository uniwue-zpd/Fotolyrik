package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PersonPreviewDTO;
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

    public Person PreviewDTOToPerson(PersonPreviewDTO personPreviewDTO) {
        if (personPreviewDTO == null || personPreviewDTO.getId() == null) return null;
        return personRepository.findById(personPreviewDTO.getId()).orElse(null);
    }

    public Set<Person> PreviewDTOsToPersons(Set<PersonPreviewDTO> personPreviewDTOS) {
        if (personPreviewDTOS.isEmpty()) return Collections.emptySet();
        return personPreviewDTOS.stream()
                .map(this::PreviewDTOToPerson)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public PersonPreviewDTO PersonToPreviewDTO(Person person) {
        if (person == null) return null;
        PersonPreviewDTO personPreviewDTO = new PersonPreviewDTO();
        personPreviewDTO.setId(person.getId());
        personPreviewDTO.setFullName(person.getFullName());
        personPreviewDTO.setStudioName(person.getStudioName());
        personPreviewDTO.setPseudonyms(person.getPseudonyms());
        return personPreviewDTO;
    }

    public Set<PersonPreviewDTO> PersonsToPersonDTOs(Set<Person> persons) {
        if (persons.isEmpty()) return Collections.emptySet();
        return persons.stream()
                .map(this::PersonToPreviewDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
