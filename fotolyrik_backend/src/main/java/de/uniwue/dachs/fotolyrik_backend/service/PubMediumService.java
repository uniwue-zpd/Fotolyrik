package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PubMediumService {
    private final PubMediumRepository pubMediumRepository;
    private final PlaceRepository placeRepository;

    public PubMediumService(PubMediumRepository pubMediumRepository,
                            PlaceRepository placeRepository) {
        this.pubMediumRepository = pubMediumRepository;
        this.placeRepository = placeRepository;
    }

    // GET
    public List<PubMedium> getAllPubMedia() {
        return pubMediumRepository.findAll();
    }

    // GET by ID
    public Optional<PubMedium> getPubMediumById(Long id) {
        return pubMediumRepository.findById(id);
    }

    // POST
    @Transactional
    public PubMedium savePubMedium(PubMedium pubMedium) {
        pubMedium.setPublication_places(getOrSavePubPlace(pubMedium.getPublication_places()));
        return pubMediumRepository.save(pubMedium);
    }

    //TODO: Implement method for PUT Mapping

    // DELETE
    @Transactional
    public void deletePubPlace(Long id) {
        if (!pubMediumRepository.existsById(id)) {
            throw new RuntimeException("Photopoem with id '" + id + "' does not exist");
        }
        else {
            pubMediumRepository.deleteById(id);
        }
    }

    // Helper
    private Set<Place> getOrSavePubPlace(Set<Place> pub_places) {
        if (pub_places == null || pub_places.isEmpty()) {
            return new HashSet<>();
        }
        Set<Place> savedPlaces = new HashSet<>();
        for (Place place : pub_places) {
            if (place.getId() != null) {
                savedPlaces.add(placeRepository.findById(place.getId()).orElse(null));
            }
            else {
                savedPlaces.add(placeRepository.save(place));
            }
        }
        return savedPlaces;
    }
}
