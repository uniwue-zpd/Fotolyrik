package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.IDSliceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PubMediumPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PubMediumMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import de.uniwue.dachs.fotolyrik_backend.service.PubMediumService;
import de.uniwue.dachs.fotolyrik_backend.specification.PubMediumSearchCriteria;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publication_media")
public class PubMediumController {
    private final PubMediumService pubMediumService;

    public PubMediumController(PubMediumService pubMediumService) {
        this.pubMediumService = pubMediumService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PubMediumDTO>> getAllPubMedia() {
        return ResponseEntity.ok(pubMediumService.getAllPubMedia());
    }

    /**
     * Default view of the publication medium search: a paginated list of full {@link PubMediumDTO} objects.
     * Handles requests without a {@code view} parameter or with {@code view=paginated}. All filters are optional.
     * {@code query} matches title, subtitle or both combined; {@code title} and {@code subtitle} filter separately.
     */
    @GetMapping
    public ResponseEntity<Page<PubMediumDTO>> getPaginatedPubMedia(
            Pageable pageable,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String subtitle,
            @RequestParam(required = false, value = "pubplace-id") Long pubPlaceId,
            @RequestParam(required = false, value = "pubplace") String pubPlace,
            @RequestParam(required = false, value = "publisher-id") Long publisherId,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false, value = "publication-rhythm-id") Long pubRhythmId,
            @RequestParam(required = false, value = "publication-rhythm") String pubRhythm,
            @RequestParam(required = false, value = "editorial-office") String editorialOffice,
            @RequestParam(required = false, value = "start-year") Integer startYear,
            @RequestParam(required = false, value = "end-year") Integer endYear,
            @RequestParam(required = false, value = "amount-volumes") Long amountVolumes,
            @RequestParam(required = false, value = "amount-issues") Long amountIssues,
            @RequestParam(required = false, value = "zdb-id") String zdbId
    ) {
        PubMediumSearchCriteria criteria = new PubMediumSearchCriteria(
                query, title, subtitle, pubPlaceId, pubPlace, publisherId, publisher,
                pubRhythmId, pubRhythm, editorialOffice, startYear, endYear,
                amountVolumes, amountIssues, zdbId
        );
        return ResponseEntity.ok(pubMediumService.searchPaginated(criteria, pageable));
    }

    /**
     * Flat view of the publication medium search ({@code view=flat}): an unpaginated list of lightweight
     * {@link PubMediumPreviewDTO} objects. Accepts the same filters as the paginated view; all are optional.
     */
    @GetMapping(params = "view=flat")
    public ResponseEntity<List<PubMediumPreviewDTO>> getFlatPubMedia(
            Sort sort,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String subtitle,
            @RequestParam(required = false, value = "pubplace-id") Long pubPlaceId,
            @RequestParam(required = false, value = "pubplace") String pubPlace,
            @RequestParam(required = false, value = "publisher-id") Long publisherId,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false, value = "publication-rhythm-id") Long pubRhythmId,
            @RequestParam(required = false, value = "publication-rhythm") String pubRhythm,
            @RequestParam(required = false, value = "editorial-office") String editorialOffice,
            @RequestParam(required = false, value = "start-year") Integer startYear,
            @RequestParam(required = false, value = "end-year") Integer endYear,
            @RequestParam(required = false, value = "amount-volumes") Long amountVolumes,
            @RequestParam(required = false, value = "amount-issues") Long amountIssues,
            @RequestParam(required = false, value = "zdb-id") String zdbId
    ) {
        PubMediumSearchCriteria criteria = new PubMediumSearchCriteria(
                query, title, subtitle, pubPlaceId, pubPlace, publisherId, publisher,
                pubRhythmId, pubRhythm, editorialOffice, startYear, endYear,
                amountVolumes, amountIssues, zdbId
        );
        return ResponseEntity.ok(pubMediumService.searchFlat(criteria, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PubMediumDTO> getPubMediumById(@PathVariable Long id) {
        return pubMediumService.getPubMediumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/{id}/neighbor")
    public ResponseEntity<IDSliceDTO> getPubMediumNeighborIDs(@PathVariable Long id) {
        return pubMediumService.getPubMediumNeighborIds(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PubMediumDTO> createPubMedium(@RequestBody PubMediumDTO pubMediumDTO) {
        PubMediumDTO createdPubMedium = pubMediumService.createPubMedium(pubMediumDTO);
        return ResponseEntity.status(201).body(createdPubMedium);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PubMediumDTO> updatePubMedium(@PathVariable Long id, @RequestBody PubMediumDTO pubMediumDTO) {
        try {
            return ResponseEntity.ok(pubMediumService.updatePubMedium(id, pubMediumDTO));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePubMedium(@PathVariable Long id) {
        try {
            pubMediumService.deletePubMedium(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping(value = "/stats", params = {"type=same-place", "view=graph"})
    public ResponseEntity<GraphDTO> getSamePlaceGraph() {
        return ResponseEntity.ok(pubMediumService.getSamePlaceGraph());
    }

    @GetMapping(value = "/{id}/stats", params = "type=metrics")
    public ResponseEntity<PubMediumMetricsDTO> getPubMediumMetrics(@PathVariable Long id) {
        return ResponseEntity.ok(pubMediumService.getPubMediumMetrics(id));
    }
}
