package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.PublisherDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PublisherPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.service.PublisherService;
import de.uniwue.dachs.fotolyrik_backend.specification.PublisherSearchCriteria;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PublisherDTO>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    /**
     * Default view of the publisher search: a paginated list of full {@link PublisherDTO} objects.
     * Handles requests without a {@code view} parameter or with {@code view=paginated}. All filters are optional.
     */
    @GetMapping
    public ResponseEntity<Page<PublisherDTO>> getPaginatedPublishers(
            Pageable pageable,
            @RequestParam(required = false) String name
    ) {
        PublisherSearchCriteria criteria = new PublisherSearchCriteria(name);
        return ResponseEntity.ok(publisherService.searchPaginated(criteria, pageable));
    }

    /**
     * Flat view of the publisher search ({@code view=flat}): an unpaginated list of lightweight
     * {@link PublisherPreviewDTO} objects. All filters are optional.
     */
    @GetMapping(params = "view=flat")
    public ResponseEntity<List<PublisherPreviewDTO>> getFlatPublishers(
            Sort sort,
            @RequestParam(required = false) String name
    ) {
        PublisherSearchCriteria criteria = new PublisherSearchCriteria(name);
        return ResponseEntity.ok(publisherService.searchFlat(criteria, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisher(@PathVariable Long id) {
        return publisherService.getPublisherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@RequestBody PublisherDTO publisherDTO) {
        PublisherDTO createdPublisher = publisherService.createPublisher(publisherDTO);
        return ResponseEntity.status(201).body(createdPublisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable Long id, @RequestBody PublisherDTO publisherDTO) {
        try {
            PublisherDTO updatedPublisher = publisherService.updatePublisher(id, publisherDTO);
            return ResponseEntity.status(201).body(updatedPublisher);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        try {
            publisherService.deletePublisherById(id);
            return ResponseEntity.status(204).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
}
