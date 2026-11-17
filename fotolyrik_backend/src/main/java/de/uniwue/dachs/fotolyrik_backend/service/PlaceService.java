package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.previews.PlacePreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.PlaceMetricsDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.visualization.graph.GraphDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import de.uniwue.dachs.fotolyrik_backend.specification.PlaceSearchCriteria;
import de.uniwue.dachs.fotolyrik_backend.specification.PlaceSpecification;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.GraphMapper;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PlaceMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;
    private final GraphMapper graphMapper;

    public PlaceService(PlaceRepository placeRepository, PlaceMapper placeMapper, GraphMapper graphMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
        this.graphMapper = graphMapper;
    }

    /**
     * GET all places sorted by name
     * @return {@link List} of {@link Place} as {@link PlaceDTO}
     */
    public List<PlaceDTO> getAllPlaces() {
        return placeMapper.PlacesToPlaceDTOs(placeRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    public Optional<PlaceDTO> getPlaceById(Long id) {
        return placeRepository.findById(id).map(placeMapper::PlaceToPlaceDTO);
    }

    @Transactional
    public PlaceDTO createPlace(PlaceDTO placeDTO) {
        var entity = placeMapper.PlaceDTOToPlace(placeDTO);
        var savedEntity = placeRepository.save(entity);
        return placeMapper.PlaceToPlaceDTO(savedEntity);
    }

    @Transactional
    public PlaceDTO updatePlace(Long id, PlaceDTO place) {
        return placeRepository.findById(id)
                .map(entity -> {
                    entity.updateBaseEntityNotes(place);
                    entity.setName(place.getName());
                    entity.setDescription(place.getDescription());
                    entity.setLatitude(place.getLatitude());
                    entity.setLongitude(place.getLongitude());
                    return placeRepository.save(entity);
                }).map(placeMapper::PlaceToPlaceDTO)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id '" + id + "' can't be updated"));
    }

    @Transactional
    public void deletePlace(Long id) {
        if (!placeRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity with id '" + id + "' can't be deleted");
        } else {
            placeRepository.deleteById(id);
        }
    }

    /**
     * GET a page of places matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of full {@link PlaceDTO} objects
     */
    public Page<PlaceDTO> searchPaginated(PlaceSearchCriteria criteria, Pageable pageable) {
        return placeRepository.findAll(buildSpecification(criteria), pageable)
                .map(placeMapper::PlaceToPlaceDTO);
    }

    /**
     * GET an unpaginated list of place previews matching the given criteria
     * @param criteria optional filters; unset values are ignored
     * @param sort sort order; falls back to ascending name if unsorted
     * @return a {@link List} of {@link PlacePreviewDTO} objects
     */
    public List<PlacePreviewDTO> searchFlat(PlaceSearchCriteria criteria, Sort sort) {
        Sort effectiveSort = sort != null && sort.isSorted() ? sort : Sort.by("name").ascending();
        return placeMapper.PlacesToPlacePreviewDTOs(
                placeRepository.findAll(buildSpecification(criteria), effectiveSort));
    }

    /**
     * Builds a {@link Specification} that combines all set criteria with AND.
     * @param c search criteria; {@code null} values and blank strings are ignored
     */
    private Specification<Place> buildSpecification(PlaceSearchCriteria c) {
        Specification<Place> spec = Specification.where(null);
        if (c == null) return spec;
        if (hasText(c.name())) spec = spec.and(PlaceSpecification.hasName(c.name()));
        return spec;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * GET metrics of a place with given ID
     * @param placeId ID of the place
     * @return a {@link PlaceMetricsDTO} with the metrics of the place
     */
    public PlaceMetricsDTO getPlaceMetrics(Long placeId) {
        return placeRepository.getMetricsByPlace(placeId);
    }

    public GraphDTO getSamePublicationMediumGraph(){
        return graphMapper.fromAdjacencyList(placeRepository.findAdjacencyList());
    }
}
