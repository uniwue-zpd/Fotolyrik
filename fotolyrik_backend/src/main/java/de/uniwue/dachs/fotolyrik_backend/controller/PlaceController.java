package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/places")
public class PlaceController {
    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping
    public Iterable<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Place getPlaceById(@PathVariable Long id) {
        return placeRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Place addPlace(@RequestBody Place place) {
        return placeRepository.save(place);
    }

    @PutMapping("/{id}")
    public Place updatePlace(@PathVariable Long id, @RequestBody Place place) {
        Place existingPlace = placeRepository.findById(id).orElse(null);
        if (existingPlace == null) {
            return null;
        }
        existingPlace.setName(place.getName());
        existingPlace.setDescription(place.getDescription());
        existingPlace.setLatitude(place.getLatitude());
        existingPlace.setLongitude(place.getLongitude());

        return placeRepository.save(existingPlace);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable Long id) {
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id);
            return ResponseEntity.ok("Place with id '" + id + "' deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
