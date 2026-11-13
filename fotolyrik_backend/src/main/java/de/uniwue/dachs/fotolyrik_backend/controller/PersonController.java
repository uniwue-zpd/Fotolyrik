package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.IDSliceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PersonDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PersonPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.KeywordCountDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PersonMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import de.uniwue.dachs.fotolyrik_backend.service.PersonService;
import de.uniwue.dachs.fotolyrik_backend.specification.PersonSearchCriteria;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        List<PersonDTO> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    /**
     * Default view of the person search: a paginated list of full {@link PersonDTO} objects.
     * Handles requests without a {@code view} parameter or with {@code view=paginated}. All filters are optional.
     */
    @GetMapping
    public ResponseEntity<Page<PersonDTO>> getPaginatedPersons(
            Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, value = "studio-name") String studioName,
            @RequestParam(required = false) String pseudonym,
            @RequestParam(required = false, value = "gnd-id") String gndId,
            @RequestParam(required = false) String sex,
            @RequestParam(required = false, value = "birth-year") Integer birthYear,
            @RequestParam(required = false, value = "birth-year-from") Integer birthYearFrom,
            @RequestParam(required = false, value = "birth-year-to") Integer birthYearTo,
            @RequestParam(required = false, value = "death-year") Integer deathYear,
            @RequestParam(required = false, value = "death-year-from") Integer deathYearFrom,
            @RequestParam(required = false, value = "death-year-to") Integer deathYearTo
    ) {
        PersonSearchCriteria criteria = new PersonSearchCriteria(
                name, studioName, pseudonym, gndId, sex,
                birthYear, birthYearFrom, birthYearTo, deathYear, deathYearFrom, deathYearTo
        );
        return ResponseEntity.ok(personService.searchPaginated(criteria, pageable));
    }

    /**
     * Flat view of the person search ({@code view=flat}): an unpaginated list of lightweight
     * {@link PersonPreviewDTO} objects. At least one filter is required; use {@code /all} for an unfiltered list.
     */
    @GetMapping(params = "view=flat")
    public ResponseEntity<List<PersonPreviewDTO>> getFlatPersons(
            Sort sort,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, value = "studio-name") String studioName,
            @RequestParam(required = false) String pseudonym,
            @RequestParam(required = false, value = "gnd-id") String gndId,
            @RequestParam(required = false) String sex,
            @RequestParam(required = false, value = "birth-year") Integer birthYear,
            @RequestParam(required = false, value = "birth-year-from") Integer birthYearFrom,
            @RequestParam(required = false, value = "birth-year-to") Integer birthYearTo,
            @RequestParam(required = false, value = "death-year") Integer deathYear,
            @RequestParam(required = false, value = "death-year-from") Integer deathYearFrom,
            @RequestParam(required = false, value = "death-year-to") Integer deathYearTo
    ) {
        PersonSearchCriteria criteria = new PersonSearchCriteria(
                name, studioName, pseudonym, gndId, sex,
                birthYear, birthYearFrom, birthYearTo, deathYear, deathYearFrom, deathYearTo
        );
        if (criteria.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(personService.searchFlat(criteria, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/{id}/neighbor")
    public ResponseEntity<IDSliceDTO> getPersonNeighborIDs(@PathVariable Long id) {
        return personService.getPersonNeighborIds(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        PersonDTO createdPerson = personService.createPerson(personDTO);
        return ResponseEntity.status(201).body(createdPerson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        try {
            PersonDTO updatedPerson = personService.updatePerson(id, personDTO);
            return ResponseEntity.status(201).body(updatedPerson);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePerson(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/stats/graph/worked_with")
    public ResponseEntity<GraphDTO> getWorkedWithGraph() {
        return ResponseEntity.ok(personService.getWorkedWithGraph());
    }
    @GetMapping("/{id}/stats/contribution_places")
    public ResponseEntity<List<PlaceDTO>> getContributionPlacesByPersonId(@PathVariable Long id) {
        try {
            var contributionPlaces = personService.getContributionPlaces(id);
            return ResponseEntity.ok(contributionPlaces);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/{id}/stats/themes")
    public ResponseEntity<List<KeywordCountDTO>> getThemesByAuthor(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "10") Long limit) {
        List<KeywordCountDTO> themes = personService.findTopThemesByAuthor(id, limit);
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/{id}/stats/image-motifs")
    public ResponseEntity<List<KeywordCountDTO>> getImageMotifsByAuthor(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "10") Long limit) {
        List<KeywordCountDTO> imageMotifs = personService.findTopImageMotifsByAuthor(id, limit);
        return ResponseEntity.ok(imageMotifs);
    }

    @GetMapping("/{id}/stats/metrics")
    public ResponseEntity<PersonMetricsDTO> getPersonMetrics(@PathVariable Long id) {
        PersonMetricsDTO metrics = personService.getPersonMetrics(id);
        return ResponseEntity.ok(metrics);
    }
}
