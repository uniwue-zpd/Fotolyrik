package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.service.KeywordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController {
    private final KeywordService keywordService;

    public KeywordController(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @GetMapping
    public ResponseEntity<List<KeywordDTO>> getAllKeywords() {
        List<KeywordDTO> keywords = keywordService.getAllKeywords();
        return ResponseEntity.ok(keywords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeywordDTO> getKeywordById(@PathVariable Long id) {
        return keywordService.getKeywordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<KeywordDTO> createKeyword(@RequestBody KeywordDTO keywordDTO) {
        KeywordDTO createdKeyword = keywordService.createKeyword(keywordDTO);
        return ResponseEntity.status(201).body(createdKeyword);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeywordDTO> updateKeyword(@PathVariable Long id, @RequestBody KeywordDTO keywordDTO) {
        try {
            KeywordDTO updatedKeyword = keywordService.updateKeyword(id, keywordDTO);
            return ResponseEntity.status(201).body(updatedKeyword);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Long id) {
        try {
            keywordService.deleteKeyword(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
    @GetMapping("/search_paginated")
    public ResponseEntity<Page<KeywordDTO>> searchKeywordPaginated(Pageable pageable, @RequestParam String query) {
        Page<KeywordDTO> keywords = keywordService.searchKeywordsPaginated(pageable, query);
        return ResponseEntity.ok(keywords);
    }
}
