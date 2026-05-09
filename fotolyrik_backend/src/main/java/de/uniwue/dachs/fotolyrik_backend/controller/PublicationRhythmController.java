package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.PublicationRhythmDTO;
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
    public ResponseEntity<List<PublicationRhythmDTO>> getPublicationRhythms() {
        List<PublicationRhythmDTO> publicationRhythms = publicationRhythmService.getAllPublicationRhythms();
        return ResponseEntity.ok().body(publicationRhythms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationRhythmDTO> getPublicationRhythmById(@PathVariable Long id) {
        return publicationRhythmService.getPublicationRhythmById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<PublicationRhythmDTO> createPublicationRhythm(@RequestBody PublicationRhythmDTO publicationRhythmDTO) {
        PublicationRhythmDTO createdPublicationRhythm = publicationRhythmService.createPublicationRhythm(publicationRhythmDTO);
        return ResponseEntity.status(201).body(createdPublicationRhythm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicationRhythmDTO> updatePublicationRhythm(@PathVariable Long id, @RequestBody PublicationRhythmDTO publicationRhythmDTO) {
        try {
            PublicationRhythmDTO updatedPublicationRhythm = publicationRhythmService.updatePublicationRhythm(id, publicationRhythmDTO);
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
