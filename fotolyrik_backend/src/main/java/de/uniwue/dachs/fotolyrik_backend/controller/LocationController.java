package de.uniwue.dachs.fotolyrik_backend.controller;

import de.uniwue.dachs.fotolyrik_backend.DTO.LocationDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Location;
import de.uniwue.dachs.fotolyrik_backend.service.LocationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<LocationDTO> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        LocationDTO createdLocation = locationService.createLocation(locationDTO);
        return ResponseEntity.status(201).body(createdLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @RequestBody LocationDTO locationDTO) {
        try {
            LocationDTO updatedLocation = locationService.updateLocation(id, locationDTO);
            return ResponseEntity.status(201).body(updatedLocation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }
}
