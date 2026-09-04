package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.IDSliceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PersonDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PersonPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.KeywordCountDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PersonMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.AdjacencyProjection;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import de.uniwue.dachs.fotolyrik_backend.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        List<PersonDTO> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
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

    @GetMapping("/search")
    public ResponseEntity<List<PersonPreviewDTO>> searchPeople(@RequestParam String query) {
        if (query == null || query.trim().length() < 2) return ResponseEntity.ok(List.of());
        List<PersonPreviewDTO> persons = personService.searchPeople(query);
        return ResponseEntity.ok(persons);
    }
    @GetMapping("/search_paginated")
    public ResponseEntity<Page<PersonPreviewDTO>> searchPeoplePaginated(Pageable pageable, @RequestParam String query) {
        Page<PersonPreviewDTO> persons = personService.searchPeoplePaginated(pageable, query);
        return ResponseEntity.ok(persons);
    }
}
