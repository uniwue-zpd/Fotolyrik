package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.FullText;
import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.service.FullTextService;
import de.uniwue.dachs.fotolyrik_backend.service.PhotopoemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/photopoems")
public class PhotopoemController {
    private final PhotopoemService photopoemService;
    private final FullTextService fullTextService;

    public PhotopoemController(PhotopoemService photopoemService, FullTextService fullTextService) {
        this.photopoemService = photopoemService;
        this.fullTextService = fullTextService;
    }

    @GetMapping
    public ResponseEntity<List<Photopoem>> getPhotopoems() {
        List<Photopoem> photopoems = photopoemService.getAllPhotopoems();
        return ResponseEntity.ok(photopoems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Photopoem> getPhotopoemById(@PathVariable Long id) {
        return photopoemService.getPhotopoemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/{id}/fulltext")
    public ResponseEntity<FullText> getFullTextByPhotopoemId(@PathVariable Long id) {
        return fullTextService.getFullTextByPhotopoemId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Photopoem>> filterByAuthorPhotographer(
            @RequestParam (value = "author_id", required = false) Long author_id,
            @RequestParam (value = "photographer_id", required = false) Long photographer_id) {
        List<Photopoem> photopoems = new ArrayList<>();
        if (author_id != null && photographer_id != null) {
            photopoems = photopoemService.getPhotopoemsByAuthorIdAndPhotographerId(author_id, photographer_id);
        } else if (author_id != null) {
            photopoems = photopoemService.getPhotopoemsByAuthorId(author_id);
        } else if (photographer_id != null) {
            photopoems = photopoemService.getPhotopoemsByPhotographerId(photographer_id);
        }
        return ResponseEntity.ok(photopoems);
    }

    @PostMapping
    public ResponseEntity<Photopoem> savePhotopoem(@RequestBody Photopoem photopoem) {
        Photopoem savedPhotopoem = photopoemService.savePhotopoem(photopoem);
        return ResponseEntity.status(201).body(savedPhotopoem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Photopoem> updatePhotopoem(@PathVariable Long id, @RequestBody Photopoem photopoem) {
        try {
            Photopoem updatedPhotopoem = photopoemService.updatePhotopoem(id, photopoem);
            return ResponseEntity.ok(updatedPhotopoem);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}/fulltext")
    public ResponseEntity<FullText> updateFullTextByPhotopoem(@PathVariable Long id, @RequestBody FullText fullText) {
        try {
            FullText updatedFullText = fullTextService.updateFullTextByPhotopoemId(id, fullText.getFull_text());
            return ResponseEntity.ok(updatedFullText);
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhotopoem(@PathVariable Long id) {
        try {
            photopoemService.deletePhotopoem(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
