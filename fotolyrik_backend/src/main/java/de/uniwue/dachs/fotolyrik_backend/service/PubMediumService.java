package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.IDSliceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PubMediumDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PubMediumPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PubMediumMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import de.uniwue.dachs.fotolyrik_backend.model.PubMedium;
import de.uniwue.dachs.fotolyrik_backend.repository.PubMediumRepository;
import de.uniwue.dachs.fotolyrik_backend.specification.PubMediumSearchCriteria;
import de.uniwue.dachs.fotolyrik_backend.specification.PubMediumSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.GraphMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PlaceMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PubMediumMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublicationRhythmMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PublisherMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PubMediumService {
    private final PubMediumRepository pubMediumRepository;
    private final PubMediumMapper pubMediumMapper;
    private final PlaceMapper placeMapper;
    private final PublisherMapper publisherMapper;
    private final PublicationRhythmMapper publicationRhythmMapper;
    private final GraphMapper graphMapper;

    public PubMediumService(PubMediumRepository pubMediumRepository,
                            PubMediumMapper pubMediumMapper,
                            PlaceMapper placeMapper,
                            PublisherMapper publisherMapper,
                            PublicationRhythmMapper publicationRhythmMapper, GraphMapper graphMapper) {
        this.pubMediumRepository = pubMediumRepository;
        this.pubMediumMapper = pubMediumMapper;
        this.placeMapper = placeMapper;
        this.publisherMapper = publisherMapper;
        this.publicationRhythmMapper = publicationRhythmMapper;
        this.graphMapper = graphMapper;
    }

    /**
     * @return a {@link List} of all {@link PubMediumDTO} objects sorted by title
     */
    public List<PubMediumDTO> getAllPubMedia() {
        return pubMediumRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))
                .stream()
                .map(pubMediumMapper::PubMediumToPubMediumDTO)
                .toList();
    }

    /**
     * GET a page of publication media matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of full {@link PubMediumDTO} objects
     */
    public Page<PubMediumDTO> searchPaginated(PubMediumSearchCriteria criteria, Pageable pageable) {
        return pubMediumRepository.findAll(buildSpecification(criteria), pageable)
                .map(pubMediumMapper::PubMediumToPubMediumDTO);
    }

    /**
     * GET an unpaginated list of publication medium previews matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param sort sort order; falls back to ascending title if unsorted
     * @return a {@link List} of {@link PubMediumPreviewDTO} objects
     */
    public List<PubMediumPreviewDTO> searchFlat(PubMediumSearchCriteria criteria, Sort sort) {
        Sort effectiveSort = sort != null && sort.isSorted() ? sort : Sort.by("title").ascending();
        return pubMediumRepository.findAll(buildSpecification(criteria), effectiveSort)
                .stream()
                .map(pubMediumMapper::PubMediumToPubMediumPreviewDTO)
                .filter(Objects::nonNull)
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
     * GET the next and previous IDs of a pub medium by ID sorted by title ASC, subtitle ASC
     * @param id of the current pub medium
     * @return {@link Optional} of {@link IDSliceDTO}
     */
    public Optional<IDSliceDTO> getPubMediumNeighborIds(Long id) {
        var list = pubMediumRepository.findNeighborIdsById(id, 1);

        int currentIndex = list.indexOf(id);
        if (currentIndex == -1) return Optional.empty();

        var slice = new IDSliceDTO();
        slice.setCurrent(id);
        slice.setPrevious(currentIndex > 0 ? list.get(currentIndex - 1) : null);
        slice.setNext(currentIndex < list.size() - 1 ? list.get(currentIndex + 1) : null);

        return Optional.of(slice);
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
    public void deletePubMedium(Long id) {
        if (!pubMediumRepository.existsById(id)) {
            throw new EntityNotFoundException("PubMedium with id '" + id + "' does not exist");
        }
        pubMediumRepository.deleteById(id);
    }

    /**
     * GET metrics of a pub medium with given ID
     * @param pubMediumId ID of the pub medium
     * @return a {@link PubMediumMetricsDTO} with the metrics of the pub medium
     */
    public PubMediumMetricsDTO getPubMediumMetrics(Long pubMediumId) {
        return pubMediumRepository.getMetricsByPubMedium(pubMediumId);
    }

    public GraphDTO getSamePlaceGraph() {
        return graphMapper.fromAdjacencyList(pubMediumRepository.findMediumAdjacencyListByPlace());
    }

    /**
     * Builds a {@link Specification} that combines all set criteria with AND.
     * @param c search criteria; {@code null} values and blank strings are ignored
     */
    private Specification<PubMedium> buildSpecification(PubMediumSearchCriteria c) {
        Specification<PubMedium> spec = Specification.where(null);
        if (c == null) return spec;
        if (hasText(c.query())) spec = spec.and(PubMediumSpecification.hasTitleOrSubtitle(c.query()));
        if (hasText(c.title())) spec = spec.and(PubMediumSpecification.hasTitle(c.title()));
        if (hasText(c.subtitle())) spec = spec.and(PubMediumSpecification.hasSubtitle(c.subtitle()));
        if (c.pubPlaceId() != null) spec = spec.and(PubMediumSpecification.hasPubPlaceId(c.pubPlaceId()));
        if (hasText(c.pubPlace())) spec = spec.and(PubMediumSpecification.hasPubPlace(c.pubPlace()));
        if (c.publisherId() != null) spec = spec.and(PubMediumSpecification.hasPublisherId(c.publisherId()));
        if (hasText(c.publisher())) spec = spec.and(PubMediumSpecification.hasPublisher(c.publisher()));
        if (c.pubRhythmId() != null) spec = spec.and(PubMediumSpecification.hasPubRhythmId(c.pubRhythmId()));
        if (hasText(c.pubRhythm())) spec = spec.and(PubMediumSpecification.hasPubRhythm(c.pubRhythm()));
        if (hasText(c.editorialOffice())) spec = spec.and(PubMediumSpecification.hasEditorialOffice(c.editorialOffice()));
        if (c.startYear() != null) spec = spec.and(PubMediumSpecification.hasStartYear(c.startYear()));
        if (c.endYear() != null) spec = spec.and(PubMediumSpecification.hasEndYear(c.endYear()));
        if (c.amountVolumes() != null) spec = spec.and(PubMediumSpecification.hasAmountVolumes(c.amountVolumes()));
        if (c.amountIssues() != null) spec = spec.and(PubMediumSpecification.hasAmountIssues(c.amountIssues()));
        if (hasText(c.zdbId())) spec = spec.and(PubMediumSpecification.hasZdbId(c.zdbId()));
        return spec;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
