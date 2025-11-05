package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.repository.KeywordRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class KeywordMapper {
    private final KeywordRepository keywordRepository;

    public KeywordMapper(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public Keyword KeywordDTOToKeyword(KeywordDTO keywordDTO) {
        if (keywordDTO == null) return null;
        if (keywordDTO.getId() != null) {
            return keywordRepository.findById(keywordDTO.getId()).orElse(null);
        } else {
            Keyword keyword = new Keyword();
            keyword.setValue(keywordDTO.getValue());
            keywordRepository.save(keyword);
            return keyword;
        }
    }

    public Set<Keyword> KeywordDTOsToKeywords(Set<KeywordDTO> keywordDTOs) {
        if (keywordDTOs.isEmpty()) return Collections.emptySet();
        return keywordDTOs.stream()
                .map(this::KeywordDTOToKeyword)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public KeywordDTO KeywordToKeywordDTO(Keyword keyword) {
        if (keyword == null) return null;
        KeywordDTO keywordDTO = new KeywordDTO();
        keywordDTO.setId(keyword.getId());
        keywordDTO.setValue(keyword.getValue());
        return  keywordDTO;
    }

    public Set<KeywordDTO> KeywordToKeywordDTOs(Set<Keyword> keywords) {
        if (keywords.isEmpty()) return Collections.emptySet();
        return keywords.stream()
                .map(this::KeywordToKeywordDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
