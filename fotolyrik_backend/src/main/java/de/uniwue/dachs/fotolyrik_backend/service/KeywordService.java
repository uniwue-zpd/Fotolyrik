package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PersonPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.model.Person;
import de.uniwue.dachs.fotolyrik_backend.repository.KeywordRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.KeywordMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final KeywordMapper keywordMapper;

    public KeywordService(KeywordRepository keywordRepository, KeywordMapper keywordMapper) {
        this.keywordRepository = keywordRepository;
        this.keywordMapper = keywordMapper;
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

    // DELETE keyword by ID
    @Transactional
    public void deleteKeyword(Long id) {
        if (!keywordRepository.existsById(id)) {
            throw new EntityNotFoundException("Keyword with ID '" + id + "' does not exist");
        }
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
}
