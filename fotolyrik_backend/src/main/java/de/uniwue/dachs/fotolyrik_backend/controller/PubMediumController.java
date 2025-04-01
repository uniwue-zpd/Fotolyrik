package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publication_media")
public class PubMediumController {
    private final PubMediumRepository pubMediumRepository;

    public PubMediumController(PubMediumRepository pubMediumRepository) {
        this.pubMediumRepository = pubMediumRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable<PubMedium>> getPubMediums() {
        Iterable<PubMedium> pubMediums = pubMediumRepository.findAll();
        return ResponseEntity.ok(pubMediums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PubMedium> getPubMediumById(@PathVariable Long id) {
        return pubMediumRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<PubMedium> savePubMedium(@RequestBody PubMedium pubMedium) {
        PubMedium savedPubMedium = pubMediumRepository.save(pubMedium);
        return ResponseEntity.status(201).body(savedPubMedium);
    }

    //TODO: Implement both PUT and DELETE mappings
    //TODO: Implement service layer for PubMedium
}
