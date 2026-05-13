package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import de.uniwue.dachs.fotolyrik_backend.specification.PubMediumSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PlaceMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PubMediumMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublicationRhythmMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublisherMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        return pubMediumRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
                .stream()
                .map(pubMediumMapper::PubMediumToPubMediumDTO)
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
     * Filters {@link PubMedium} entities based on the provided criteria.
     * @param title refers to the title of the publication medium
     * @param subtitle refers to the subtitle of the publication medium
     * @param pubPlaceId refers to the ID of the publication place
     * @param pubPlace refers to the name of the publication place
     * @param publisherId refers to the ID of the publisher
     * @param publisher refers to the name of the publisher
     * @param pubRhythmId refers to the ID of the publication rhythm
     * @param pubRhythm refers to the name of the publication rhythm
     * @param editorialOffice refers to the editorial office of the publication medium
     * @param startYear refers to the start year of the publication medium
     * @param endYear refers to the end year of the publication medium
     * @param amountVolumes refers to the amount of volumes of the publication medium
     * @param amountIssues refers to the amount of issues of the publication medium
     * @param zdbId refers to the ZDB ID of the publication medium
     * @return a {@link List} of filtered {@link PubMediumDTO} objects
     */
    public List<PubMediumDTO> filterPubMedia(
            String title,
            String subtitle,
            Long pubPlaceId,
            String pubPlace,
            Long publisherId,
            String publisher,
            Long pubRhythmId,
            String pubRhythm,
            String editorialOffice,
            Long startYear,
            Long endYear,
            Long amountVolumes,
            Long amountIssues,
            String zdbId

    ) {
        Specification<PubMedium> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and(PubMediumSpecification.hasTitle(title));
        }
        if (subtitle != null && !subtitle.isEmpty()) {
            spec = spec.and(PubMediumSpecification.hasSubtitle(subtitle));
        }
        if (pubPlaceId != null) {
            spec = spec.and(PubMediumSpecification.hasPubPlaceId(pubPlaceId));
        }
        if (pubPlace != null && !pubPlace.isEmpty()) {
            spec = spec.and(PubMediumSpecification.hasPubPlace(pubPlace));
        }
        if (publisherId != null) {
            spec = spec.and(PubMediumSpecification.hasPublisherId(publisherId));
        }
        if (publisher != null && !publisher.isEmpty()) {
            spec = spec.and(PubMediumSpecification.hasPublisher(publisher));
        }
        if (pubRhythmId != null) {
            spec = spec.and(PubMediumSpecification.hasPubRhythmId(pubRhythmId));
        }
        if (pubRhythm != null && !pubRhythm.isEmpty()) {
            spec = spec.and(PubMediumSpecification.hasPubRhythm(pubRhythm));
        }
        if (editorialOffice != null && !editorialOffice.isEmpty()) {
            spec = spec.and(PubMediumSpecification.hasEditorialOffice(editorialOffice));
        }
        if (startYear != null) {
            spec = spec.and(PubMediumSpecification.hasStartYear(startYear));
        }
        if (endYear != null) {
            spec = spec.and(PubMediumSpecification.hasEndYear(endYear));
        }
        if (amountVolumes != null) {
            spec = spec.and(PubMediumSpecification.hasAmountVolumes(amountVolumes));
        }
        if (amountIssues != null) {
            spec = spec.and(PubMediumSpecification.hasAmountIssues(amountIssues));
        }
        if (zdbId != null && !zdbId.isEmpty()) {
            spec = spec.and(PubMediumSpecification.hasZdbId(zdbId));
        }

        List<PubMedium> result = pubMediumRepository.findAll(spec);
        return result.stream()
                .map(pubMediumMapper::PubMediumToPubMediumDTO)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(PubMediumDTO::getTitle))
                .toList();
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
    @Transactional
    public PubMediumDTO updatePubMedium(Long id, PubMediumDTO updatedPubMediumDTO) {
        return pubMediumRepository.findById(id)
                .map(entity -> {
                    entity.updateBaseEntityNotes(updatedPubMediumDTO);
                    entity.setTitle(updatedPubMediumDTO.getTitle());
                    entity.setSubtitle(updatedPubMediumDTO.getSubtitle());
                    entity.setPublicationPlaces(placeMapper.PlacePreviewDTOsToPlaces(updatedPubMediumDTO.getPublicationPlaces()));
                    entity.setPublisher(publisherMapper.PublisherPreviewDTOToPublisher(updatedPubMediumDTO.getPublisher()));
                    entity.setPubRhytms(publicationRhythmMapper.PublicationRhythmPreviewDTOsToPublicationRhythms(updatedPubMediumDTO.getPubRhythms()));
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
