package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.IDSliceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PersonDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PersonPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.KeywordCountDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PersonMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import de.uniwue.dachs.fotolyrik_backend.model.File;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.FileRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PersonRepository;
import de.uniwue.dachs.fotolyrik_backend.specification.PersonSearchCriteria;
import de.uniwue.dachs.fotolyrik_backend.specification.PersonSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PersonMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PlaceMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.GraphMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final FileRepository fileRepository;
    private final PersonMapper personMapper;
    private final PlaceMapper placeMapper;
    private final GraphMapper graphMapper;

    public PersonService(PersonRepository personRepository, FileRepository fileRepository, PersonMapper personMapper, PlaceMapper placeMapper, GraphMapper graphMapper) {
        this.personRepository = personRepository;
        this.fileRepository = fileRepository;
        this.personMapper = personMapper;
        this.placeMapper = placeMapper;
        this.graphMapper = graphMapper;
    }

    /**
     * GET all persons sorted by first name and last name
     * @return {@link List} of {@link Person} as {@link PersonDTO}
     */
    public List<PersonDTO> getAllPersons() {
        return personMapper.PersonsToPersonDTOs(personRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName", "lastName")));
    }

    /**
     * GET person by ID
     * @param id of the person
     * @return {@link Optional} of {@link Person} as {@link PersonDTO}
     */
    public Optional<PersonDTO> getPersonById(Long id) {
        return personRepository.findById(id).map(personMapper::PersonToPersonDTO);
    }

    /**
     * GET the next and previous IDs of a person by ID sorted by full name ASC
     * @param id of the current person
     * @return {@link Optional} of {@link IDSliceDTO}
     */
    public Optional<IDSliceDTO> getPersonNeighborIds(Long id) {
        var list = personRepository.findNeighborIdsById(id, 1);

        int currentIndex = list.indexOf(id);
        if (currentIndex == -1) return Optional.empty();

        var slice = new IDSliceDTO();
        slice.setCurrent(id);
        slice.setPrevious(currentIndex > 0 ? list.get(currentIndex - 1) : null);
        slice.setNext(currentIndex < list.size() - 1 ? list.get(currentIndex + 1) : null);

        return Optional.of(slice);
    }

    /**
     * POST create a new person
     * @param personDTO {@link PersonDTO} to create
     * @return the {@link PersonDTO} of {@link Person} created
     */
    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {
        var entity = personMapper.PersonDTOToPerson(personDTO);
        entity.setImage(entity.getImage() != null
                ? getImage(entity.getImage().getId())
                : null);
        var savedEntity = personRepository.save(entity);
        return personMapper.PersonToPersonDTO(savedEntity);
    }

    /**
     * PUT update an existing person
     * @param id of the person to update
     * @param updatedPerson with updated values
     * @return {@link PersonDTO} of updated {@link Person}
     */
    @Transactional
    public PersonDTO updatePerson(Long id, PersonDTO updatedPerson) {
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
                }).map(personMapper::PersonToPersonDTO)
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
     * GET places the person contributed in by following
     * person->contribution->photopoem->pub_medium->place
     * @return {@link List} of {@link PlaceDTO}
     */
    public List<PlaceDTO> getContributionPlaces(Long personId){
        return placeMapper.PlacesToPlaceDTOs(personRepository.findContributionPlacesByPersonId(personId));
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

    /**
     * GET top x themes by author with given Id and limit
     * @param authorId ID of the author
     * @param limit limit of themes to return
     * @return {@link List} of {@link KeywordCountDTO} with the top themes and their count
     */
    public List<KeywordCountDTO> findTopThemesByAuthor(Long authorId, Long limit) {
        return personRepository.findTopThemesByPerson(authorId, limit);
    }

    /**
     * GET top x image motifs by author with given Id and limit
     * @param authorId ID of the author
     * @param limit limit of image motifs to return
     * @return {@link List} of {@link KeywordCountDTO} with the top image motifs and their count
     */
    public List<KeywordCountDTO> findTopImageMotifsByAuthor(Long authorId, Long limit) {
        return personRepository.findTopImageMotifsByPerson(authorId, limit);
    }

    /**
     * GET metrics of a person with given ID
     * @param personId ID of the person
     * @return a {@link PersonMetricsDTO} with the metrics of the person
     */
    public PersonMetricsDTO getPersonMetrics(Long personId) {
        return personRepository.getMetricsByPerson(personId);
    }

    /**
     * GET a page of persons matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of full {@link PersonDTO} objects
     */
    public Page<PersonDTO> searchPaginated(PersonSearchCriteria criteria, Pageable pageable) {
        return personRepository.findAll(buildSpecification(criteria), pageable)
                .map(personMapper::PersonToPersonDTO);
    }

    /**
     * GET an unpaginated list of person previews matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param sort sort order; falls back to ascending last name if unsorted
     * @return a {@link List} of {@link PersonPreviewDTO} objects
     */
    public List<PersonPreviewDTO> searchFlat(PersonSearchCriteria criteria, Sort sort) {
        Sort effectiveSort = sort != null && sort.isSorted() ? sort : Sort.by("lastName").ascending();
        return personMapper.PersonsToPreviewDTOs(
                personRepository.findAll(buildSpecification(criteria), effectiveSort));
    }

    /**
     * Builds a {@link Specification} that combines all set criteria with AND.
     * @param c search criteria; {@code null} values and blank strings are ignored
     */
    private Specification<Person> buildSpecification(PersonSearchCriteria c) {
        Specification<Person> spec = Specification.where(null);
        if (c == null) return spec;
        if (hasText(c.name())) spec = spec.and(PersonSpecification.hasName(c.name()));
        if (hasText(c.studioName())) spec = spec.and(PersonSpecification.hasStudioName(c.studioName()));
        if (hasText(c.pseudonym())) spec = spec.and(PersonSpecification.hasPseudonym(c.pseudonym()));
        if (hasText(c.gndId())) spec = spec.and(PersonSpecification.hasGndId(c.gndId()));
        if (hasText(c.sex())) spec = spec.and(PersonSpecification.hasSex(c.sex()));
        if (c.birthYear() != null) spec = spec.and(PersonSpecification.hasBirthYear(c.birthYear()));
        if (c.birthYearFrom() != null || c.birthYearTo() != null)
            spec = spec.and(PersonSpecification.birthYearBetween(c.birthYearFrom(), c.birthYearTo()));
        if (c.deathYear() != null) spec = spec.and(PersonSpecification.hasDeathYear(c.deathYear()));
        if (c.deathYearFrom() != null || c.deathYearTo() != null)
            spec = spec.and(PersonSpecification.deathYearBetween(c.deathYearFrom(), c.deathYearTo()));
        return spec;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    public GraphDTO getWorkedWithGraph() {
        return graphMapper.fromAdjacencyList(personRepository.findAdjacencyList());
    }
}
