package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.GndSuggestionDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.KeywordPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.service.GndLookupService;
import de.uniwue.dachs.fotolyrik_backend.service.KeywordService;
import de.uniwue.dachs.fotolyrik_backend.specification.KeywordSearchCriteria;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController {
    private final KeywordService keywordService;
    private final GndLookupService gndLookupService;

    public KeywordController(KeywordService keywordService, GndLookupService gndLookupService) {
        this.keywordService = keywordService;
        this.gndLookupService = gndLookupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<KeywordDTO>> getAllKeywords() {
        return ResponseEntity.ok(keywordService.getAllKeywords());
    }

    /**
     * Default view of the keyword search: a paginated list of {@link KeywordPreviewDTO} objects
     * (id, value, GND ID). Handles requests without a {@code view} parameter or with {@code view=paginated}.
     * All filters are optional.
     */
    @GetMapping
    public ResponseEntity<Page<KeywordPreviewDTO>> getPaginatedKeywords(
            Pageable pageable,
            @RequestParam(required = false) String value,
            @RequestParam(required = false, value = "gnd-id") String gndId
    ) {
        KeywordSearchCriteria criteria = new KeywordSearchCriteria(value, gndId);
        return ResponseEntity.ok(keywordService.searchPaginated(criteria, pageable));
    }

    /**
     * Flat view of the keyword search ({@code view=flat}): an unpaginated list of lightweight
     * {@link KeywordPreviewDTO} objects. Accepts the same filters as the paginated view; all are optional.
     */
    @GetMapping(params = "view=flat")
    public ResponseEntity<List<KeywordPreviewDTO>> getFlatKeywords(
            Sort sort,
            @RequestParam(required = false) String value,
            @RequestParam(required = false, value = "gnd-id") String gndId
    ) {
        KeywordSearchCriteria criteria = new KeywordSearchCriteria(value, gndId);
        return ResponseEntity.ok(keywordService.searchFlat(criteria, sort));
    }

    /**
     * Suggests GND subject headings ("Sachbegriffe") for a term via lobid-gnd.
     * It uses the lobid-gnd API to find suggestions for the given term.
     * A suggestion with {@code exactMatch = true} means the term itself is a valid GND keyword.
     * Returns 502 if lobid is unavailable.
     */
    @GetMapping("/gnd-suggestions")
    public ResponseEntity<List<GndSuggestionDTO>> getGndSuggestions(@RequestParam String term) {
        try {
            return ResponseEntity.ok(gndLookupService.getSuggestions(term));
        } catch (RestClientException e) {
            return ResponseEntity.status(502).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeywordDTO> getKeywordById(@PathVariable Long id) {
        return keywordService.getKeywordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<KeywordDTO> createKeyword(@RequestBody KeywordDTO keywordDTO) {
        try {
            return ResponseEntity.status(201).body(keywordService.createKeyword(keywordDTO));
        } catch (EntityExistsException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeywordDTO> updateKeyword(@PathVariable Long id, @RequestBody KeywordDTO keywordDTO) {
        try {
            return ResponseEntity.ok(keywordService.updateKeyword(id, keywordDTO));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        } catch (EntityExistsException e) {
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Long id) {
        try {
            keywordService.deleteKeyword(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
