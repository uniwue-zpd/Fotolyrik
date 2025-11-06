package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.service.PubMediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/publication_media")
public class PubMediumController {
    private final PubMediumService pubMediumService;

    public PubMediumController(PubMediumService pubMediumService) {
        this.pubMediumService = pubMediumService;
    }

    @GetMapping
    public ResponseEntity<List<PubMediumDTO>> getPubMediums() {
        List<PubMediumDTO> pubMedia = pubMediumService.getAllPubMedia();
        return ResponseEntity.ok(pubMedia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PubMediumDTO> getPubMediumById(@PathVariable Long id) {
        return pubMediumService.getPubMediumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<PubMediumDTO> savePubMedium(@RequestBody PubMediumDTO pubMediumDTO) {
        PubMediumDTO savedPubMedium = pubMediumService.createPubMedium(pubMediumDTO);
        return ResponseEntity.status(201).body(savedPubMedium);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PubMediumDTO> updatePubMedium(@PathVariable Long id, @RequestBody PubMediumDTO pubMediumDTO) {
        try {
            PubMediumDTO updated = pubMediumService.updatePubMedium(id, pubMediumDTO);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePubMedium(@PathVariable Long id) {
        try {
            pubMediumService.deletePubPlace(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
