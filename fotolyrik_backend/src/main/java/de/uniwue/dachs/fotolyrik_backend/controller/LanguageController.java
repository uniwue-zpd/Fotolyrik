package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.LanguageDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Language;
import de.uniwue.dachs.fotolyrik_backend.service.LanguageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public ResponseEntity<List<LanguageDTO>> getAllLanguages() {
        List<LanguageDTO> languages = languageService.getAllLanguages();
        return ResponseEntity.ok().body(languages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDTO> getLanguageById(@PathVariable Long id) {
        return languageService.getLanguageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<LanguageDTO> createLanguage(@RequestBody LanguageDTO languageDTO) {
        LanguageDTO createdLanguage = languageService.createLanguage(languageDTO);
        return ResponseEntity.status(201).body(createdLanguage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageDTO> updateLanguage(@PathVariable Long id, @RequestBody LanguageDTO languageDTO) {
        try {
            LanguageDTO updatedLanguage = languageService.updateLanguage(id, languageDTO);
            return ResponseEntity.status(201).body(updatedLanguage);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        try {
            languageService.deleteLanguageById(id);
            return ResponseEntity.status(204).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
