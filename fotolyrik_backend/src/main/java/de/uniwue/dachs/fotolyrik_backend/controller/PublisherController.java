package de.uniwue.dachs.fotolyrik_backend.controller;

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
    public ResponseEntity<List<Publisher>> getPublishers() {
        List<Publisher> publishers = publisherService.getAllPublishers();
        return ResponseEntity.ok().body(publishers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisher(@PathVariable Long id) {
        return publisherService.getPublisherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) {
        Publisher createdPublisher = publisherService.createPublisher(publisher);
        return ResponseEntity.status(201).body(createdPublisher);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @RequestBody Publisher publisher) {
        try {
            Publisher updatedPublisher = publisherService.updatePublisher(id, publisher);
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
