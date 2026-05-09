package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.FileDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PersonFullDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PersonPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonMapper {
    private final PersonRepository personRepository;
    private final FileMapper fileMapper;

    public PersonMapper(PersonRepository personRepository, FileMapper fileMapper) {
        this.personRepository = personRepository;
        this.fileMapper = fileMapper;
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
    public Person PersonFullDTOToPerson(PersonFullDTO personFullDTO) {
        if (personFullDTO == null) return null;
        if (personFullDTO.getId() != null){
            return personRepository.findById(personFullDTO.getId()).orElse(null); } else{
            Person person = new Person();
            person.setFirstName(personFullDTO.getFirstName());
            person.setLastName(personFullDTO.getLastName());
            person.setStudioName(personFullDTO.getStudioName());
            person.setPseudonyms(personFullDTO.getPseudonyms());
            person.setBirthYear(personFullDTO.getBirthYear());
            person.setDeathYear(personFullDTO.getDeathYear());
            person.setSex(personFullDTO.getSex());
            person.setGndId(personFullDTO.getGndId());
            person.setNotes(personFullDTO.getNotes());
            person.setImage(fileMapper.FileDTOToFile(personFullDTO.getImage()));
            personRepository.save(person);
            return person;
        }
    }
    public PersonFullDTO PersonToPersonFullDTO(Person person){
        if (person == null) return null;
        PersonFullDTO personFullDTO = new PersonFullDTO();
        personFullDTO.setId(person.getId());
        personFullDTO.setFirstName(person.getFirstName());
        personFullDTO.setLastName(person.getLastName());
        personFullDTO.setStudioName(person.getStudioName());
        personFullDTO.setFullName(person.getFullName());
        personFullDTO.setPseudonyms(person.getPseudonyms());
        personFullDTO.setBirthYear(person.getBirthYear());
        personFullDTO.setDeathYear(person.getDeathYear());
        personFullDTO.setSex(person.getSex());
        personFullDTO.setGndId(person.getGndId());
        personFullDTO.setNotes(person.getNotes());
        personFullDTO.setImage(fileMapper.FileToFileDTO(person.getImage()));
        personFullDTO.setBaseEntityFields(person);
        return personFullDTO;
    }
    public Set<PersonFullDTO> PersonsToPersonFullDTOs(Set<Person> persons) {
        return MapperUtils.mapSet(persons, this::PersonToPersonFullDTO);
    }

    public Set<Person> PersonFullDTOsToPersons(Set<PersonFullDTO> personFullDTOS) {
        return MapperUtils.mapSet(personFullDTOS, this::PersonFullDTOToPerson);
    }
    public List<PersonFullDTO> PersonsToPersonFullDTOs(List<Person> persons) {
        return MapperUtils.mapList(persons, this::PersonToPersonFullDTO);
    }

    public List<Person> PersonFullDTOsToPersons(List<PersonFullDTO> personFullDTOS) {
        return MapperUtils.mapList(personFullDTOS, this::PersonFullDTOToPerson);
    }
}
