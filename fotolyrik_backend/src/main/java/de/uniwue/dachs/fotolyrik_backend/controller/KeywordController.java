package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.service.KeywordService;
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
    public ResponseEntity<List<Keyword>> getAllKeywords() {
        List<Keyword> keywords = keywordService.getAllKeywords();
        return ResponseEntity.ok(keywords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Keyword> getKeywordById(@PathVariable Long id) {
        return keywordService.getKeywordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<Keyword> createKeyword(@RequestBody Keyword keyword) {
        Keyword createdKeyword = keywordService.createKeyword(keyword);
        return ResponseEntity.status(201).body(createdKeyword);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Keyword> updateKeyword(@PathVariable Long id, @RequestBody Keyword updatedKeyword) {
        Keyword keyword = keywordService.updateKeyword(id, updatedKeyword);
        if (keyword != null) {
            return ResponseEntity.status(201).body(keyword);
        } else {
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
}
