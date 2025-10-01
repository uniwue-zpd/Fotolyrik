package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.repository.KeywordRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    // GET all keywords
    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }

    // GET keyword by ID
    public Optional<Keyword> getKeywordById(Long id) {
        return keywordRepository.findById(id);
    }

    // POST create new keyword
    @Transactional
    public Keyword createKeyword(Keyword keyword) {
        return keywordRepository.save(keyword);
    }

    // PUT update existing keyword
    @Transactional
    public Keyword updateKeyword(Long id, Keyword updatedKeyword) {
        return keywordRepository.findById(id)
                .map(existingKeyword -> {
                    existingKeyword.setValue(updatedKeyword.getValue());
                    existingKeyword.setGndId(updatedKeyword.getGndId());
                    return keywordRepository.save(existingKeyword);
                })
                .orElse(null);
    }

    // DELETE keyword by ID
    @Transactional
    public void deleteKeyword(Long id) {
        if (!keywordRepository.existsById(id)) {
            throw new EntityNotFoundException("Keyword with ID '" + id + "' does not exist");
        }
        keywordRepository.deleteById(id);
    }
}
