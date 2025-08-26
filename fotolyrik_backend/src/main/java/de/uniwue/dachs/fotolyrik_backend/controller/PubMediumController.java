package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.service.PubMediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/publication_media")
public class PubMediumController {
    private final PubMediumService pubMediumService;

    public PubMediumController(PubMediumService pubMediumService) {
        this.pubMediumService = pubMediumService;
    }

    @GetMapping
    public ResponseEntity<Iterable<PubMedium>> getPubMediums() {
        Iterable<PubMedium> pubMediums = pubMediumService.getAllPubMedia();
        return ResponseEntity.ok(pubMediums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PubMedium> getPubMediumById(@PathVariable Long id) {
        return pubMediumService.getPubMediumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<PubMedium> savePubMedium(@RequestBody PubMedium pubMedium) {
        PubMedium savedPubMedium = pubMediumService.savePubMedium(pubMedium);
        return ResponseEntity.status(201).body(savedPubMedium);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PubMedium> updatePubMedium(@PathVariable Long id, @RequestBody PubMedium pubMedium) {
        try {
            PubMedium updated = pubMediumService.updatePubMedium(id, pubMedium);
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
