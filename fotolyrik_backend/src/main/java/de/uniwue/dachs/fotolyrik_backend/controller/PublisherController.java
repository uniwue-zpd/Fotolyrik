package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.PublisherDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Publisher;
import de.uniwue.dachs.fotolyrik_backend.service.PublisherService;
import jakarta.persistence.EntityNotFoundException;
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

    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getPublishers() {
        List<PublisherDTO> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok().body(publishers);
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
