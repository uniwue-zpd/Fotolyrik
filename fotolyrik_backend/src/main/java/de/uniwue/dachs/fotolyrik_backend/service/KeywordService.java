package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.repository.KeywordRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PhotopoemRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.KeywordMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final KeywordMapper keywordMapper;
    private final PhotopoemRepository photopoemRepository;

    public KeywordService(KeywordRepository keywordRepository, KeywordMapper keywordMapper, PhotopoemRepository photopoemRepository) {
        this.keywordRepository = keywordRepository;
        this.keywordMapper = keywordMapper;
        this.photopoemRepository = photopoemRepository;
    }

    // GET all keywords
    public List<KeywordDTO> getAllKeywords() {
        return keywordMapper.KeywordToKeywordDTOs(keywordRepository.findAll(Sort.by(Sort.Direction.ASC, "value")));
    }

    // GET keyword by ID
    public Optional<KeywordDTO> getKeywordById(Long id) {
        return keywordRepository.findById(id).map(keywordMapper::KeywordToKeywordDTO);
    }

    // POST create new keyword
    @Transactional
    public KeywordDTO createKeyword(KeywordDTO keywordDTO) {
        var entity = keywordMapper.KeywordDTOToKeyword(keywordDTO);
        var savedEntity = keywordRepository.save(entity);
        return keywordMapper.KeywordToKeywordDTO(savedEntity);
    }

    // PUT update existing keyword
    @Transactional
    public KeywordDTO updateKeyword(Long id, KeywordDTO updatedKeyword) {
        return keywordRepository.findById(id)
                .map(existingKeyword -> {
                    existingKeyword.updateBaseEntityNotes(updatedKeyword);
                    existingKeyword.setValue(updatedKeyword.getValue());
                    existingKeyword.setGndId(updatedKeyword.getGndId());
                    return keywordRepository.save(existingKeyword);
                }).map(keywordMapper::KeywordToKeywordDTO)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    /**
     * Deletes a keyword by its ID. Before deletion, it detaches the keyword from all photopoems that reference it, either as a theme or an image motif.
     * If the keyword with the specified ID does not exist, an EntityNotFoundException is thrown.
     * @param id the ID of the keyword to delete
     * @throws EntityNotFoundException if the keyword with the specified ID does not exist
     */
    @Transactional
    public void deleteKeyword(Long id) {
        if (!keywordRepository.existsById(id)) {
            throw new EntityNotFoundException("Keyword with ID '" + id + "' does not exist");
        }
        detachKeywordByIdFromPhotopoems(id);
        keywordRepository.deleteById(id);
    }

    // GET a list of keywords constrained by QUERY and PAGEABLE
    public Page<KeywordDTO> searchKeywordsPaginated(Pageable pageable, String query) {
        Page<Keyword> result;
        if (query  == null||  query.trim().length()<2){
            result =  keywordRepository.findAll(pageable);
        } else {
            result = keywordRepository.findByValueContainingIgnoreCase(query, pageable);
        }
        return result.map(keywordMapper::KeywordToKeywordDTO);
    }

    /**
     * Detach a keyword from all photopoems that reference it, either as a theme or an image motif.
     * @param id the ID of the keyword to detach
     */
    private void detachKeywordByIdFromPhotopoems(Long id) {
        Set<Photopoem> photopoemsWithKeyword = new HashSet<>();
        photopoemsWithKeyword.addAll(photopoemRepository.findByThemes_Id(id));
        photopoemsWithKeyword.addAll(photopoemRepository.findByImageMotifs_Id(id));
        photopoemsWithKeyword.forEach(photopoem -> {
            photopoem.getThemes().removeIf(keyword -> keyword.getId().equals(id));
            photopoem.getImageMotifs().removeIf(keyword -> keyword.getId().equals(id));
        });
    }
}
