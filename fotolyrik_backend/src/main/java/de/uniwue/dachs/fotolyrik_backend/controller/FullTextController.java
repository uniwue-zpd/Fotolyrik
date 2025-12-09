package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextSearchResult;
import de.uniwue.dachs.fotolyrik_backend.service.FullTextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fulltexts")
public class FullTextController {
    private final FullTextService fullTextService;

    public FullTextController(FullTextService fullTextService) {
        this.fullTextService = fullTextService;
    }

    @GetMapping
    public ResponseEntity<List<FullTextDTO>> getFullTexts() {
        List<FullTextDTO> fullTexts = fullTextService.getAllFullTexts();
        return ResponseEntity.ok(fullTexts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullTextDTO> getFullTextById(@PathVariable Long id) {
        return fullTextService.getFullTextById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<FullTextDTO> saveFullText(@RequestBody FullTextDTO fullTextDTO) {
        FullTextDTO savedFullText = fullTextService.createFullText(fullTextDTO);
        return ResponseEntity.status(201).body(savedFullText);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FullTextDTO> updateFullText(@PathVariable Long id, @RequestBody FullTextDTO fullTextDTO) {
        try {
            FullTextDTO updatedFullText = fullTextService.updateFullText(id, fullTextDTO);
            return ResponseEntity.ok(updatedFullText);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFullText(@PathVariable Long id) {
        try {
            fullTextService.deleteFullText(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<FullTextSearchResult>> searchFullText(@RequestParam String query) {
        List<FullTextSearchResult> results = fullTextService.searchFullText(query);
        return ResponseEntity.ok(results);
    }
}
