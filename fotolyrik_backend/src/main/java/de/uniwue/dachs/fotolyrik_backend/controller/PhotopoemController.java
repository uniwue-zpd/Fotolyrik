package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.Photopoem;
import de.uniwue.dachs.fotolyrik_backend.service.PhotopoemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/photopoems")
public class PhotopoemController {
    private final PhotopoemService photopoemService;

    public PhotopoemController(PhotopoemService photopoemService) {
        this.photopoemService = photopoemService;
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
