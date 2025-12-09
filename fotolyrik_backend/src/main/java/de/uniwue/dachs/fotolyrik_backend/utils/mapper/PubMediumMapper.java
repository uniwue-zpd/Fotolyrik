package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import org.springframework.stereotype.Component;

@Component
public class PubMediumMapper {
    private final PubMediumRepository pubMediumRepository;
    private final PlaceMapper placeMapper;
    private final PublisherMapper publisherMapper;
    private final PublicationRhythmMapper publicationRhythmMapper;

    public  PubMediumMapper(PubMediumRepository pubMediumRepository, PlaceMapper placeMapper, PublisherMapper publisherMapper, PublicationRhythmMapper publicationRhythmMapper) {
        this.pubMediumRepository = pubMediumRepository;
        this.placeMapper = placeMapper;
        this.publisherMapper = publisherMapper;
        this.publicationRhythmMapper = publicationRhythmMapper;
    }

    public PubMedium PubMediumPreviewDTOToPubMedium(PubMediumPreviewDTO pubMediumPreviewDTO) {
        if (pubMediumPreviewDTO == null) return null;
        if (pubMediumPreviewDTO.getId() != null) {
            return pubMediumRepository.findById(pubMediumPreviewDTO.getId()).orElse(null);
        } else {
            PubMedium pubMedium = new PubMedium();
            pubMedium.setTitle(pubMediumPreviewDTO.getTitle());
            pubMediumRepository.save(pubMedium);
            return pubMedium;
        }
    }

    public PubMediumPreviewDTO PubMediumToPubMediumPreviewDTO(PubMedium pubMedium) {
        if (pubMedium == null) return null;
        PubMediumPreviewDTO pubMediumPreviewDTO = new PubMediumPreviewDTO();
        pubMediumPreviewDTO.setId(pubMedium.getId());
        pubMediumPreviewDTO.setTitle(pubMedium.getTitle());
        return pubMediumPreviewDTO;
    }

    public PubMedium PubMediumDTOToPubMedium(PubMediumDTO pubMediumDTO) {
        PubMedium pubMedium = new PubMedium();
        pubMedium.setTitle(pubMediumDTO.getTitle());
        pubMedium.setSubtitle(pubMediumDTO.getSubtitle());
        pubMedium.setPublicationPlaces(placeMapper.PlaceDTOsToPlaces(pubMediumDTO.getPublicationPlaces()));
        pubMedium.setPublisher(publisherMapper.PublisherDTOToPublisher(pubMediumDTO.getPublisher()));
        pubMedium.setPubRhytms(publicationRhythmMapper.PublicationRhythmDTOsToPublicationRhythms(pubMediumDTO.getPubRhythms()));
        pubMedium.setEditorialOffice(pubMediumDTO.getEditorialOffice());
        pubMedium.setStartYear(pubMediumDTO.getStartYear());
        pubMedium.setEndYear(pubMediumDTO.getEndYear());
        pubMedium.setAmountVolumes(pubMediumDTO.getAmountVolumes());
        pubMedium.setAmountIssues(pubMediumDTO.getAmountIssues());
        pubMedium.setZdbId(pubMediumDTO.getZdbId());
        pubMedium.setNotes(pubMediumDTO.getNotes());
        return pubMedium;
    }

    public PubMediumDTO PubMediumToPubMediumDTO(PubMedium pubMedium) {
        PubMediumDTO pubMediumDTO = new PubMediumDTO();
        pubMediumDTO.setId(pubMedium.getId());
        pubMediumDTO.setCreatedDate(pubMedium.getCreatedDate());
        pubMediumDTO.setCreatedBy(pubMedium.getCreatedBy());
        pubMediumDTO.setLastModifiedDate(pubMedium.getLastModifiedDate());
        pubMediumDTO.setLastModifiedBy(pubMedium.getLastModifiedBy());
        pubMediumDTO.setTitle(pubMedium.getTitle());
        pubMediumDTO.setSubtitle(pubMedium.getSubtitle());
        pubMediumDTO.setPublicationPlaces(placeMapper.PlacesToPlaceDTOs(pubMedium.getPublicationPlaces()));
        pubMediumDTO.setPublisher(publisherMapper.PublisherToPublisherDTO(pubMedium.getPublisher()));
        pubMediumDTO.setPubRhythms(publicationRhythmMapper.PublicationRhythmsToPublicationRhythmDTOs(pubMedium.getPubRhytms()));
        pubMediumDTO.setEditorialOffice(pubMedium.getEditorialOffice());
        pubMediumDTO.setStartYear(pubMedium.getStartYear());
        pubMediumDTO.setEndYear(pubMedium.getEndYear());
        pubMediumDTO.setAmountVolumes(pubMedium.getAmountVolumes());
        pubMediumDTO.setAmountIssues(pubMedium.getAmountIssues());
        pubMediumDTO.setZdbId(pubMedium.getZdbId());
        pubMediumDTO.setNotes(pubMedium.getNotes());
        return pubMediumDTO;
    }
}
