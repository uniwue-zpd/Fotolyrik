package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PlacePreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PlaceMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import de.uniwue.dachs.fotolyrik_backend.service.PlaceService;
import de.uniwue.dachs.fotolyrik_backend.specification.PlaceSearchCriteria;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlaceDTO>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    /**
     * Default view of the place search: a paginated list of full {@link PlaceDTO} objects.
     * Handles requests without a {@code view} parameter or with {@code view=paginated}. All filters are optional.
     */
    @GetMapping
    public ResponseEntity<Page<PlaceDTO>> getPaginatedPlaces(
            Pageable pageable,
            @RequestParam(required = false) String name
    ) {
        PlaceSearchCriteria criteria = new PlaceSearchCriteria(name);
        return ResponseEntity.ok(placeService.searchPaginated(criteria, pageable));
    }

    /**
     * Flat view of the place search ({@code view=flat}): an unpaginated list of lightweight
     * {@link PlacePreviewDTO} objects. All filters are optional.
     */
    @GetMapping(params = "view=flat")
    public ResponseEntity<List<PlacePreviewDTO>> getFlatPlaces(
            Sort sort,
            @RequestParam(required = false) String name
    ) {
        PlaceSearchCriteria criteria = new PlaceSearchCriteria(name);
        return ResponseEntity.ok(placeService.searchFlat(criteria, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDTO> getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<PlaceDTO> createPlace(@RequestBody PlaceDTO placeDTO) {
        PlaceDTO createdPlace = placeService.createPlace(placeDTO);
        return ResponseEntity.status(201).body(createdPlace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceDTO> updatePlace(@PathVariable Long id, @RequestBody PlaceDTO placeDTO) {
        try {
            PlaceDTO updatedPlace = placeService.updatePlace(id, placeDTO);
            return ResponseEntity.status(201).body(updatedPlace);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        try {
            placeService.deletePlace(id);
            return ResponseEntity.status(204).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping(value = "/{id}/stats", params = "type=metrics")
    public ResponseEntity<PlaceMetricsDTO> getPlaceMetrics(@PathVariable Long id) {
        PlaceMetricsDTO metrics = placeService.getPlaceMetrics(id);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping(value = "/stats", params = {"type=same-pub-medium", "view=graph"})
    public ResponseEntity<GraphDTO> getSamePubMediumGraph() {
        return ResponseEntity.ok(placeService.getSamePublicationMediumGraph());
    }
}
