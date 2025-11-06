package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PlaceMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PubMediumMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublicationRhythmMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublisherMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PubMediumService {
    private final PubMediumRepository pubMediumRepository;
    private final PubMediumMapper pubMediumMapper;
    private final PlaceMapper placeMapper;
    private final PublisherMapper publisherMapper;
    private final PublicationRhythmMapper publicationRhythmMapper;

    public PubMediumService(PubMediumRepository pubMediumRepository,
                            PubMediumMapper pubMediumMapper,
                            PlaceMapper placeMapper,
                            PublisherMapper publisherMapper,
                            PublicationRhythmMapper publicationRhythmMapper) {
        this.pubMediumRepository = pubMediumRepository;
        this.pubMediumMapper = pubMediumMapper;
        this.placeMapper = placeMapper;
        this.publisherMapper = publisherMapper;
        this.publicationRhythmMapper = publicationRhythmMapper;
    }

    /**
     * @return a {@link List} of {@link PubMediumDTO} objects
     */
    public List<PubMediumDTO> getAllPubMedia() {
        return pubMediumRepository.findAll()
                .stream()
                .map(pubMediumMapper::PubMediumToPubMediumDTO)
                .sorted(Comparator.comparing(PubMediumDTO::getId))
                .toList();
    }

    /**
     * @param id ID of the {@link PubMedium} object to be found
     * @return a {@link PubMediumDTO} object
     */
    public Optional<PubMediumDTO> getPubMediumById(Long id) {
        return pubMediumRepository.findById(id).map(pubMediumMapper::PubMediumToPubMediumDTO);
    }

    /**
     * @param pubMediumDTO {@link PubMediumDTO} projection of the {@link PubMedium} to be created
     * @return persisted {@link PubMediumDTO} object
     */
    @Transactional
    public PubMediumDTO createPubMedium(PubMediumDTO pubMediumDTO) {
        PubMedium pubMedium = pubMediumMapper.PubMediumDTOToPubMedium(pubMediumDTO);
        PubMedium createdPubMedium = pubMediumRepository.save(pubMedium);
        return pubMediumMapper.PubMediumToPubMediumDTO(createdPubMedium);
    }

    /**
     * @param id ID of the {@link PubMedium} object to be updated
     * @param updatedPubMediumDTO {@link PubMediumDTO} projection
     * @return {@link PubMediumDTO} of the updated {@link PubMedium} object
     */
    public PubMediumDTO updatePubMedium(Long id, PubMediumDTO updatedPubMediumDTO) {
        return pubMediumRepository.findById(id)
                .map(entity -> {
                    entity.setTitle(updatedPubMediumDTO.getTitle());
                    entity.setSubtitle(updatedPubMediumDTO.getSubtitle());
                    entity.setPublicationPlaces(placeMapper.PlaceDTOsToPlaces(updatedPubMediumDTO.getPublicationPlaces()));
                    entity.setPublisher(publisherMapper.PublisherDTOToPublisher(updatedPubMediumDTO.getPublisher()));
                    entity.setPubRhytms(publicationRhythmMapper.PublicationRhythmDTOsToPublicationRhythms(updatedPubMediumDTO.getPubRhythms()));
                    entity.setEditorialOffice(updatedPubMediumDTO.getEditorialOffice());
                    entity.setStartYear(updatedPubMediumDTO.getStartYear());
                    entity.setEndYear(updatedPubMediumDTO.getEndYear());
                    entity.setAmountVolumes(updatedPubMediumDTO.getAmountVolumes());
                    entity.setAmountIssues(updatedPubMediumDTO.getAmountIssues());
                    entity.setZdbId(updatedPubMediumDTO.getZdbId());
                    entity.setNotes(updatedPubMediumDTO.getNotes());

                    PubMedium savedPubMedium = pubMediumRepository.save(entity);
                    return pubMediumMapper.PubMediumToPubMediumDTO(savedPubMedium);
                })
                .orElseThrow(() -> new EntityNotFoundException("PubMedium with id '" + id + "' does not exist"));
    }

    /**
     * @param id ID of the {@link PubMedium} object to be deleted
     */
    @Transactional
    public void deletePubPlace(Long id) {
        if (!pubMediumRepository.existsById(id)) {
            throw new EntityNotFoundException("Photopoem with id '" + id + "' does not exist");
        }
        else {
            pubMediumRepository.deleteById(id);
        }
    }
}
