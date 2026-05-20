package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.FullTextSearchResult;
import de.uniwue.dachs.fotolyrik_backend.model.FullText;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.FullTextRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.FullTextMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PhotopoemMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class FullTextService {
    private final FullTextRepository fullTextRepository;

    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^\\p{L}\\p{N}\\s\"'-]");
    private final FullTextMapper fullTextMapper;
    private final PhotopoemMapper photopoemMapper;

    public FullTextService(FullTextRepository fullTextRepository, FullTextMapper fullTextMapper, PhotopoemMapper photopoemMapper) {
        this.fullTextRepository = fullTextRepository;
        this.fullTextMapper = fullTextMapper;
        this.photopoemMapper = photopoemMapper;
    }

    /**
     * @return a {@link List} of found {@link FullTextDTO} objects
     */
    public List<FullTextDTO> getAllFullTexts() {
        return fullTextRepository.findAll()
                .stream()
                .map(fullTextMapper::FulltextToFullTextDTO)
                .sorted(Comparator.comparing(FullTextDTO::getId)).toList();
    }

    /**
     * @param id ID of the {@link FullText} to be found
     * @return a {@link FullTextDTO} object
     */
    public Optional<FullTextDTO> getFullTextById(Long id) {
        return fullTextRepository.findById(id).map(fullTextMapper::FulltextToFullTextDTO);
    }

    /**
     * @param photopoem_id ID of the photopoem
     * @return a {@link FullTextDTO} object
     */
    public Optional<FullTextDTO> getFullTextByPhotopoemId(Long photopoem_id) {
        return fullTextRepository.findById(photopoem_id).map(fullTextMapper::FulltextToFullTextDTO);
    }

    /**
     * @param query is a query to be used for the full-text-search
     * @return a {@link List} of {@link FullTextSearchResult} objects
     */
    public List<FullTextSearchResult> searchFullText(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String sanitized_query = query
                .replaceAll(HTML_TAG_PATTERN.pattern(), "")
                .replaceAll(SPECIAL_CHAR_PATTERN.pattern(), "");
        return fullTextRepository.searchFullText(sanitized_query);
    }

    /**
     * @param fullTextDTO is a {@link FullTextDTO} to be passed as parameter
     * @return a {@link FullText} object and persists it in the database
     */
    @Transactional
    public FullTextDTO createFullText(FullTextDTO fullTextDTO) {
        FullText fullText = fullTextMapper.FullTextDTOToFullText(fullTextDTO);
        FullText createdFullText = fullTextRepository.save(fullText);
        return fullTextMapper.FulltextToFullTextDTO(createdFullText);
    }

    /**
     * @param id of the {@link FullText} object
     * @param updatedFullText contains data to be passed to existing {@link FullText} object
     * @return a {@link FullText} object and persists it in the database
     */
    @Transactional
    public FullTextDTO updateFullText(Long id, FullTextDTO updatedFullText) {
        return fullTextRepository.findById(id).map(entity -> {
            entity.updateBaseEntityNotes(updatedFullText);
            entity.setFullText(updatedFullText.getFullText());
            entity.setPhotopoem(photopoemMapper.PhotopoemPreviewDTOToPhotopoem(updatedFullText.getPhotopoem()));
            FullText savedEntity = fullTextRepository.save(entity);
            return fullTextMapper.FulltextToFullTextDTO(savedEntity);
        }).orElseThrow(() -> new EntityNotFoundException("FullText with id '" + id + "' does not exist"));
    }

    /**
     * @param photopoemId ID of the corresponding photopoem
     * @param fullTextContent text
     * @return updated {@link FullText} object
     */
    @Transactional
    public FullText updateFullTextByPhotopoemId(Long photopoemId, String fullTextContent) {
        return fullTextRepository.findByPhotopoemId(photopoemId).map(entity -> {
            entity.setFullText(fullTextContent);
            return fullTextRepository.save(entity);
        }).orElseThrow(() -> new EntityNotFoundException("FullText for Photopoem with id '" + photopoemId + "' does not exist"));
    }

    /**
     * @param id ID of the {@link FullText} object to be deleted
     */
    @Transactional
    public void deleteFullText(Long id) {
        if (!fullTextRepository.existsById(id)) {
            throw new EntityNotFoundException("FullText with id '" + id + "' does not exist");
        }
        fullTextRepository.deleteById(id);
    }

    /**
     * @param photopoemId ID of the corresponding {@link Photopoem}
     */
    @Transactional
    public void deleteFullTextByPhotopoemID(Long photopoemId) {
        Optional<FullText> fullText = fullTextRepository.findByPhotopoemId(photopoemId);
        fullText.ifPresent(fullTextRepository::delete);
    }
}
