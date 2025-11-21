package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.PublicationRhythm;
import de.uniwue.dachs.fotolyrik_backend.service.PublicationRhythmService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publication_rhythms")
public class PublicationRhythmController {
    private final PublicationRhythmService publicationRhythmService;

    public PublicationRhythmController(PublicationRhythmService publicationRhythmService) {
        this.publicationRhythmService = publicationRhythmService;
    }

    @GetMapping
    public ResponseEntity<List<PublicationRhythm>> getPublicationRhythms() {
        List<PublicationRhythm> publicationRhythms = publicationRhythmService.getAllPublicationRhythms();
        return ResponseEntity.ok().body(publicationRhythms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationRhythm> getPublicationRhythmById(@PathVariable Long id) {
        return publicationRhythmService.getPublicationRhythmById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<PublicationRhythm> createPublicationRhythm(@RequestBody PublicationRhythm publicationRhythm) {
        PublicationRhythm createdPublicationRhythm = publicationRhythmService.createPublicationRhythm(publicationRhythm);
        return ResponseEntity.status(201).body(createdPublicationRhythm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicationRhythm> updatePublicationRhythm(@PathVariable Long id, @RequestBody PublicationRhythm publicationRhythm) {
        try {
            PublicationRhythm updatedPublicationRhythm = publicationRhythmService.updatePublicationRhythm(id, publicationRhythm);
            return ResponseEntity.status(201).body(updatedPublicationRhythm);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublicationRhythm(@PathVariable Long id) {
        try {
            publicationRhythmService.deletePublicationRhythmById(id);
            return ResponseEntity.status(204).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
