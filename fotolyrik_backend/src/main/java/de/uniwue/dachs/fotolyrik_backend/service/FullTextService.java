package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextSearchResult;
import de.uniwue.dachs.fotolyrik_backend.model.FullText;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.FullTextRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FullTextService {
    private final FullTextRepository fullTextRepository;
    private final PhotopoemRepository photopoemRepository;

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
        return fullTextRepository.searchFullText(query);
    }

    // POST method to save full text for a photopoem
    @Transactional
    public FullText saveFullText(FullText fullText) {
        fullText.setPhotopoem(getPhotopoem(fullText.getPhotopoem().getId()));
        return fullTextRepository.save(fullText);
    }

    // POST method to save full text by photopoem ID
    @Transactional
    public FullText saveFullTextByPhotopoemId(Long photopoemId, String fullTextContent) {
        FullText fullText = new FullText();
        fullText.setPhotopoem(getPhotopoem(photopoemId));
        fullText.setFull_text(fullTextContent);
        return fullTextRepository.save(fullText);
    }

    // PUT method to update full text by ID
    @Transactional
    public FullText updateFullText(Long id, FullText fullText) {
        return fullTextRepository.findById(id).map(entity -> {
            entity.setFull_text(fullText.getFull_text());
            entity.setPhotopoem(getPhotopoem(fullText.getPhotopoem().getId()));
            return fullTextRepository.save(entity);
        }).orElseThrow(() -> new RuntimeException("FullText with id '" + id + "' does not exist"));
    }

    // DELETE method to delete full text by ID
    @Transactional
    public void deleteFullText(Long id) {
        if (!fullTextRepository.existsById(id)) {
            throw new IllegalArgumentException("FullText with id '" + id + "' does not exist");
        }
        fullTextRepository.deleteById(id);
    }

    // DELETE method to delete full text by photopoem ID
    @Transactional
    public void deleteFullTextByPhotopoemID(Long photopoemId) {
        if (!fullTextRepository.existsByPhotopoemId(photopoemId)) {
            throw new IllegalArgumentException("FullText for Photopoem with id '" + photopoemId + "' does not exist");
        }
        fullTextRepository.deleteByPhotopoemId(photopoemId);
    }

    // Helper method to set an existing photopoem
    private Photopoem getPhotopoem(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Photopoem cannot be null");
        }
        return photopoemRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Photopoem not found with id: " + id)
        );
    }
}
