package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniwue.dachs.fotolyrik_backend.DTO.GndSuggestionDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.KeywordPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.repository.KeywordRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        keywordPreviewDTO.setGndId(keyword.getGndId());
        return  keywordPreviewDTO;
    }

    public Set<Keyword> KeywordPreviewDTOsToKeywords(Set<KeywordPreviewDTO> keywordPreviewDTOs) {
        return MapperUtils.mapSet(keywordPreviewDTOs, this::KeywordPreviewDTOToKeyword);
    }
    public Set<KeywordPreviewDTO> KeywordsToKeywordPreviewDTOs(Set<Keyword> keywords) {
        return MapperUtils.mapSet(keywords, this::KeywordToKeywordPreviewDTO);
    }
    public List<Keyword> KeywordPreviewDTOsToKeywords(List<KeywordPreviewDTO> keywordPreviewDTOs) {
        return MapperUtils.mapList(keywordPreviewDTOs, this::KeywordPreviewDTOToKeyword);
    }
    public List<KeywordPreviewDTO> KeywordsToKeywordPreviewDTOs(List<Keyword> keywords) {
        return MapperUtils.mapList(keywords, this::KeywordToKeywordPreviewDTO);
    }

    /**
     * Maps a single lobid-gnd search hit to a {@link GndSuggestionDTO}
     * @param member one entry of the {@code member} array of a lobid-gnd search response
     * @param term the search term; {@code exactMatch} is set if the preferred name equals it (case-insensitive)
     */
    public GndSuggestionDTO GndMemberToGndSuggestionDTO(JsonNode member, String term) {
        if (member == null) return null;
        GndSuggestionDTO gndSuggestionDTO = new GndSuggestionDTO();
        gndSuggestionDTO.setGndId(member.path("gndIdentifier").asText());
        gndSuggestionDTO.setPreferredName(member.path("preferredName").asText());
        gndSuggestionDTO.setExactMatch(gndSuggestionDTO.getPreferredName().equalsIgnoreCase(term.trim()));
        return gndSuggestionDTO;
    }

    /**
     * Maps a lobid-gnd search response to a list of {@link GndSuggestionDTO} objects, keeping lobid's order
     * @param response the lobid-gnd search response ({@code format=json})
     * @param term the search term
     */
    public List<GndSuggestionDTO> GndResponseToGndSuggestionDTOs(JsonNode response, String term) {
        List<GndSuggestionDTO> gndSuggestionDTOs = new ArrayList<>();
        if (response == null) return gndSuggestionDTOs;
        for (JsonNode member : response.path("member")) {
            gndSuggestionDTOs.add(GndMemberToGndSuggestionDTO(member, term));
        }
        return gndSuggestionDTOs;
    }

}
