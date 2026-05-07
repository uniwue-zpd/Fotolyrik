package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.repository.KeywordRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
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
            keyword.setGndId(keywordDTO.getGndId());
            keywordRepository.save(keyword);
            return keyword;
        }
    }

    public KeywordDTO KeywordToKeywordDTO(Keyword keyword) {
        if (keyword == null) return null;
        KeywordDTO keywordDTO = new KeywordDTO();
        keywordDTO.setId(keyword.getId());
        keywordDTO.setValue(keyword.getValue());
        keywordDTO.setGndId(keyword.getGndId());
        return  keywordDTO;
    }

    public Set<Keyword> KeywordDTOsToKeywords(Set<KeywordDTO> keywordDTOs) {
        return MapperUtils.mapSet(keywordDTOs, this::KeywordDTOToKeyword);
    }
    public Set<KeywordDTO> KeywordToKeywordDTOs(Set<Keyword> keywords) {
        return MapperUtils.mapSet(keywords, this::KeywordToKeywordDTO);
    }
    public List<Keyword> KeywordDTOsToKeywords(List<KeywordDTO> keywordDTOs) {
        return MapperUtils.mapList(keywordDTOs, this::KeywordDTOToKeyword);
    }
    public List<KeywordDTO> KeywordToKeywordDTOs(List<Keyword> keywords) {
        return MapperUtils.mapList(keywords, this::KeywordToKeywordDTO);
    }
}
