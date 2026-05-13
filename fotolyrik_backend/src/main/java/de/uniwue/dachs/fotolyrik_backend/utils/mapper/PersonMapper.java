package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PersonDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PersonPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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


    public PersonPreviewDTO PersonToPreviewDTO(Person person) {
        if (person == null) return null;
        PersonPreviewDTO personPreviewDTO = new PersonPreviewDTO();
        personPreviewDTO.setId(person.getId());
        personPreviewDTO.setFullName(person.getFullName());
        personPreviewDTO.setStudioName(person.getStudioName());
        personPreviewDTO.setPseudonyms(person.getPseudonyms());
        return personPreviewDTO;
    }

    public Person PersonDTOToPerson(PersonDTO personDTO) {
        if (personDTO == null) return null;
        if (personDTO.getId() != null){
            return personRepository.findById(personDTO.getId()).orElse(null); } else{
            Person person = new Person();
            person.setFirstName(personDTO.getFirstName());
            person.setLastName(personDTO.getLastName());
            person.setStudioName(personDTO.getStudioName());
            person.setPseudonyms(personDTO.getPseudonyms());
            person.setBirthYear(personDTO.getBirthYear());
            person.setDeathYear(personDTO.getDeathYear());
            person.setSex(personDTO.getSex());
            person.setGndId(personDTO.getGndId());
            person.setNotes(personDTO.getNotes());
            person.setImage(fileMapper.FileDTOToFile(personDTO.getImage()));
            personRepository.save(person);
            return person;
        }
    }
    public PersonDTO PersonToPersonDTO(Person person){
        if (person == null) return null;
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(person.getId());
        personDTO.setFirstName(person.getFirstName());
        personDTO.setLastName(person.getLastName());
        personDTO.setStudioName(person.getStudioName());
        personDTO.setFullName(person.getFullName());
        personDTO.setPseudonyms(person.getPseudonyms());
        personDTO.setBirthYear(person.getBirthYear());
        personDTO.setDeathYear(person.getDeathYear());
        personDTO.setSex(person.getSex());
        personDTO.setGndId(person.getGndId());
        personDTO.setNotes(person.getNotes());
        personDTO.setImage(fileMapper.FileToFileDTO(person.getImage()));
        personDTO.setBaseEntityFields(person);
        return personDTO;
    }
    public Set<PersonDTO> PersonsToPersonDTOs(Set<Person> persons) {
        return MapperUtils.mapSet(persons, this::PersonToPersonDTO);
    }

    public Set<Person> PersonDTOsToPersons(Set<PersonDTO> personDTOS) {
        return MapperUtils.mapSet(personDTOS, this::PersonDTOToPerson);
    }
    public List<PersonDTO> PersonsToPersonDTOs(List<Person> persons) {
        return MapperUtils.mapList(persons, this::PersonToPersonDTO);
    }

    public List<Person> PersonDTOsToPersons(List<PersonDTO> personDTOS) {
        return MapperUtils.mapList(personDTOS, this::PersonDTOToPerson);
    }

    public Set<PersonPreviewDTO> PersonsToPreviewDTOs(Set<Person> persons) {
        return MapperUtils.mapSet(persons, this::PersonToPreviewDTO);
    }

    public Set<Person> PreviewDTOsToPersons(Set<PersonPreviewDTO> personPreviewDTOS) {
        return MapperUtils.mapSet(personPreviewDTOS, this::PreviewDTOToPerson);
    }
    public List<PersonPreviewDTO> PersonsToPreviewDTOs(List<Person> persons) {
        return MapperUtils.mapList(persons, this::PersonToPreviewDTO);
    }

    public List<Person> PreviewDTOsToPersons(List<PersonPreviewDTO> personPreviewDTOS) {
        return MapperUtils.mapList(personPreviewDTOS, this::PreviewDTOToPerson);
    }
}
