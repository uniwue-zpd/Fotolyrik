package de.uniwue.dachs.fotolyrik_backend.service;

import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import de.uniwue.dachs.fotolyrik_backend.utils.mapper.PlaceMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    public PlaceService(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

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
}
