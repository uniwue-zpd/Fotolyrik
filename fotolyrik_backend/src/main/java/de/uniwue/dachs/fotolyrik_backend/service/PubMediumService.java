package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        pubMedium.setPublication_places(getOrSavePubPlaces(pubMedium.getPublication_places()));
        return pubMediumRepository.save(pubMedium);
    }

    // PUT
    public PubMedium updatePubMedium(Long id, PubMedium updatedPubMedium) {
        return pubMediumRepository.findById(id)
                .map(existingPubMedium -> {
                    existingPubMedium.setTitle(updatedPubMedium.getTitle());
                    existingPubMedium.setSubtitle(updatedPubMedium.getSubtitle());
                    existingPubMedium.setPublication_places(getOrSavePubPlaces(updatedPubMedium.getPublication_places()));
                    existingPubMedium.setPublisher(updatedPubMedium.getPublisher());
                    existingPubMedium.setPub_rhytm(updatedPubMedium.getPub_rhytm());
                    existingPubMedium.setStart_year(updatedPubMedium.getStart_year());
                    existingPubMedium.setEnd_year(updatedPubMedium.getEnd_year());
                    existingPubMedium.setAmount_volumes(updatedPubMedium.getAmount_volumes());
                    existingPubMedium.setAmount_issues(updatedPubMedium.getAmount_issues());
                    existingPubMedium.setZdb_id(updatedPubMedium.getZdb_id());
                    return pubMediumRepository.save(existingPubMedium);
                })
                .orElseThrow(() -> new NoSuchElementException("PubMedium with id '" + id + "' does not exist"));
    }

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
    private Set<Place> getOrSavePubPlaces(Set<Place> pub_places) {
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
