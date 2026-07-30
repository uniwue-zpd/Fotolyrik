package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PubMediumPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PersonMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PubMediumMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.service.PubMediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping("/publication_media")
public class PubMediumController {
    private final PubMediumService pubMediumService;

    public PubMediumController(PubMediumService pubMediumService) {
        this.pubMediumService = pubMediumService;
    }

    @GetMapping
    public ResponseEntity<List<PubMediumDTO>> getPubMediums() {
        List<PubMediumDTO> pubMedia = pubMediumService.getAllPubMedia();
        return ResponseEntity.ok(pubMedia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PubMediumDTO> getPubMediumById(@PathVariable Long id) {
        return pubMediumService.getPubMediumById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PubMediumDTO>> filterPubMedia(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String subtitle,
            @RequestParam(required = false, value = "pubplace-id") Long pubPlaceId,
            @RequestParam(required = false, value = "pubplace") String pubPlace,
            @RequestParam(required = false, value = "publisher-id") Long publisherId,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false, value = "publication-rhythm-id") Long publicationRhythmId,
            @RequestParam(required = false, value = "publication-rhythm") String publicationRhythm,
            @RequestParam(required = false, value = "editorial-office") String editorialOffice,
            @RequestParam(required = false, value = "start-year") Long startYear,
            @RequestParam(required = false, value = "end-year") Long endYear,
            @RequestParam(required = false, value = "amount-volumes") Long amountVolumes,
            @RequestParam(required = false, value = "amount-issues") Long amountIssues,
            @RequestParam(required = false, value = "zdb-id") String zdbId
    ) {
        List<Object> paramsCount = Stream.<Object>of(
                title, subtitle, pubPlaceId, pubPlace, publisherId, publisher,
                publicationRhythmId, publicationRhythm, editorialOffice, startYear,
                endYear, amountVolumes, amountIssues, zdbId
        )
                .filter(Objects::nonNull)
                .toList();
        if (paramsCount.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(
                pubMediumService.filterPubMedia(
                        title, subtitle, pubPlaceId, pubPlace, publisherId, publisher,
                        publicationRhythmId, publicationRhythm, editorialOffice, startYear,
                        endYear, amountVolumes, amountIssues, zdbId
                )
        );
    }

    @PostMapping
    public ResponseEntity<PubMediumDTO> savePubMedium(@RequestBody PubMediumDTO pubMediumDTO) {
        PubMediumDTO savedPubMedium = pubMediumService.createPubMedium(pubMediumDTO);
        return ResponseEntity.status(201).body(savedPubMedium);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PubMediumDTO> updatePubMedium(@PathVariable Long id, @RequestBody PubMediumDTO pubMediumDTO) {
        try {
            PubMediumDTO updated = pubMediumService.updatePubMedium(id, pubMediumDTO);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePubMedium(@PathVariable Long id) {
        try {
            pubMediumService.deletePubPlace(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/{id}/stats/metrics")
    public ResponseEntity<PubMediumMetricsDTO> getPubMediumMetrics(@PathVariable Long id) {
        PubMediumMetricsDTO metrics = pubMediumService.getPubMediumMetrics(id);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PubMediumPreviewDTO>> searchPubMedia(@RequestParam String query) {
        return ResponseEntity.ok(pubMediumService.searchPubMedia(query));
    }
}
