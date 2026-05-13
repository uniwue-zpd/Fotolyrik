package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordPreviewDTO;
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
        keywordDTO.setBaseEntityFields(keyword);
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

    public Keyword KeywordPreviewDTOToKeyword(KeywordPreviewDTO keywordPreviewDTO) {
        if (keywordPreviewDTO == null || keywordPreviewDTO.getId() == null) return null;
        return keywordRepository.findById(keywordPreviewDTO.getId()).orElse(null);
    }

    public KeywordPreviewDTO KeywordToKeywordPreviewDTO(Keyword keyword) {
        if (keyword == null) return null;
        KeywordPreviewDTO keywordPreviewDTO = new KeywordPreviewDTO();
        keywordPreviewDTO.setId(keyword.getId());
        keywordPreviewDTO.setValue(keyword.getValue());
        return  keywordPreviewDTO;
    }

    public Set<Keyword> KeywordPreviewDTOsToKeywords(Set<KeywordPreviewDTO> keywordPreviewDTOs) {
        return MapperUtils.mapSet(keywordPreviewDTOs, this::KeywordPreviewDTOToKeyword);
    }
    public Set<KeywordPreviewDTO> KeywordToKeywordPreviewDTOs(Set<Keyword> keywords) {
        return MapperUtils.mapSet(keywords, this::KeywordToKeywordPreviewDTO);
    }
    public List<Keyword> KeywordPreviewDTOsToKeywords(List<KeywordPreviewDTO> keywordPreviewDTOs) {
        return MapperUtils.mapList(keywordPreviewDTOs, this::KeywordPreviewDTOToKeyword);
    }
    public List<KeywordPreviewDTO> KeywordToKeywordPreviewDTOs(List<Keyword> keywords) {
        return MapperUtils.mapList(keywords, this::KeywordToKeywordPreviewDTO);
    }

}
