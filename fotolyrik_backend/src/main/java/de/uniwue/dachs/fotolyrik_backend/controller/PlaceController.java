package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
