package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getAllPlaces() {
        return placeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Optional<Place> getPlaceById(Long id) {
        return placeRepository.findById(id);
    }

    @Transactional
    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }

    @Transactional
    public Place updatePlace(Long id, Place place) {
        return placeRepository.findById(id)
                .map(entity -> {
                    entity.updateBaseEntityNotes(place);
                    entity.setName(place.getName());
                    entity.setDescription(place.getDescription());
                    entity.setLatitude(place.getLatitude());
                    entity.setLongitude(place.getLongitude());
                    return placeRepository.save(entity);
                }).orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    @Transactional
    public void deletePlace(Long id) {
        if (!placeRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' can't be deleted");
        } else {
            placeRepository.deleteById(id);
        }
    }
}
