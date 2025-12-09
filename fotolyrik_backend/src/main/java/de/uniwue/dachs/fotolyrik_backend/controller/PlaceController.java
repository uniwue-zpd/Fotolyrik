package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.service.PlaceService;
import jakarta.persistence.EntityNotFoundException;
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

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeService.getAllPlaces();
        return ResponseEntity.ok().body(places);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Place place) {
        Place createdPlace = placeService.createPlace(place);
        return ResponseEntity.status(201).body(createdPlace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @RequestBody Place place) {
        try {
            Place updatedPlace = placeService.updatePlace(id, place);
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
}
