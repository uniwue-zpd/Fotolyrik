package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextSearchResult;
import de.uniwue.dachs.fotolyrik_backend.model.FullText;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.FullTextRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class FullTextService {
    private final FullTextRepository fullTextRepository;
    private final PhotopoemRepository photopoemRepository;

    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^\\p{L}\\p{N}\\s\"'-]");

    public FullTextService(FullTextRepository fullTextRepository,
                           PhotopoemRepository photopoemRepository) {
        this.fullTextRepository = fullTextRepository;
        this.photopoemRepository = photopoemRepository;
    }

    // GET all
    public List<FullText> getAllFullTexts() {
        return fullTextRepository.findAll();
    }

    // GET method to get full text by ID
    public Optional<FullText> getFullTextById(Long id) {
        return fullTextRepository.findById(id);
    }

    // GET method to get full text by photopoem ID
    public Optional<FullText> getFullTextByPhotopoemId(Long photopoemId) {
        return fullTextRepository.findByPhotopoemId(photopoemId);
    }

    // GET method to search full text based on a query
    public List<FullTextSearchResult> searchFullText(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String sanitized_query = query
                .replaceAll(HTML_TAG_PATTERN.pattern(), "")
                .replaceAll(SPECIAL_CHAR_PATTERN.pattern(), "");
        return fullTextRepository.searchFullText(sanitized_query);
    }

    // POST method to save full text for a photopoem
    @Transactional
    public FullText saveFullText(FullText fullText) {
        fullText.setPhotopoem(getPhotopoem(fullText.getPhotopoem().getId()));
        return fullTextRepository.save(fullText);
    }

    // PUT method to update full text by ID
    @Transactional
    public FullText updateFullText(Long id, FullText fullText) {
        return fullTextRepository.findById(id).map(entity -> {
            entity.setFullText(fullText.getFullText());
            entity.setPhotopoem(getPhotopoem(fullText.getPhotopoem().getId()));
            return fullTextRepository.save(entity);
        }).orElseThrow(() -> new EntityNotFoundException("FullText with id '" + id + "' does not exist"));
    }

    // PUT method to update full text by photopoem ID
    @Transactional
    public FullText updateFullTextByPhotopoemId(Long photopoemId, String fullTextContent) {
        return fullTextRepository.findByPhotopoemId(photopoemId).map(entity -> {
            entity.setFullText(fullTextContent);
            return fullTextRepository.save(entity);
        }).orElseThrow(() -> new EntityNotFoundException("FullText for Photopoem with id '" + photopoemId + "' does not exist"));
    }

    // DELETE method to delete full text by ID
    @Transactional
    public void deleteFullText(Long id) {
        if (!fullTextRepository.existsById(id)) {
            throw new EntityNotFoundException("FullText with id '" + id + "' does not exist");
        }
        fullTextRepository.deleteById(id);
    }

    // DELETE method to delete full text by photopoem ID
    @Transactional
    public void deleteFullTextByPhotopoemID(Long photopoemId) {
        if (!fullTextRepository.existsByPhotopoemId(photopoemId)) {
            throw new EntityNotFoundException("FullText for Photopoem with id '" + photopoemId + "' does not exist");
        }
        fullTextRepository.deleteByPhotopoemId(photopoemId);
    }

    // Helper method to set an existing photopoem
    private Photopoem getPhotopoem(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Photopoem cannot be null");
        }
        return photopoemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Photopoem with id '" + id + "' does not exist")
        );
    }
}
