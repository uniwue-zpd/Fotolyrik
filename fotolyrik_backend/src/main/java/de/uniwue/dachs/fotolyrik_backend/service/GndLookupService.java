package de.uniwue.dachs.fotolyrik_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniwue.dachs.fotolyrik_backend.DTO.GndSuggestionDTO;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.KeywordMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Looks up GND subject headings ("Sachbegriffe im engeren Sinne") via the lobid-gnd API.
 */
@Service
public class GndLookupService {
    static final String LOBID_GND_BASE_URL = "https://lobid.org/gnd";

    private final RestClient restClient;
    private final KeywordMapper keywordMapper;

    public GndLookupService(RestClient.Builder restClientBuilder, KeywordMapper keywordMapper) {
        this.restClient = restClientBuilder.baseUrl(LOBID_GND_BASE_URL).build();
        this.keywordMapper = keywordMapper;
    }

    /**
     * GET GND subject headings matching the given term, in lobid's relevance order
     * @param term search term
     * @return a {@link List} of {@link GndSuggestionDTO} objects; {@code exactMatch} is set if the preferred name equals the term
     */
    public List<GndSuggestionDTO> getSuggestions(String term) {
        JsonNode response = restClient.get()
                .uri("/search?q={q}&filter=type:SubjectHeadingSensoStricto&format=json&size=20", escapeQuery(term.trim()))
                .retrieve()
                .body(JsonNode.class);
        return keywordMapper.GndResponseToGndSuggestionDTOs(response, term);
    }

    /**
     * Escapes characters of lobid's query syntax, so that e.g. "Haushalt (, Verbraucher)" is searched literally
     */
    static String escapeQuery(String term) {
        return term.replaceAll("([+\\-!(){}\\[\\]^\"~*?:\\\\/&|])", "\\\\$1");
    }
}
