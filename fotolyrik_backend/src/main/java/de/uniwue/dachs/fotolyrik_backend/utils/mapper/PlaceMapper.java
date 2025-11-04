package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlaceMapper {
    private final PlaceRepository placeRepository;

    public PlaceMapper(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place PlaceDTOToPlace(PlaceDTO placeDTO) {
        if (placeDTO.getId() != null) {
            return placeRepository.findById(placeDTO.getId()).orElse(null);
        }
        Place place = new Place();
        place.setName(placeDTO.getName());
        placeRepository.save(place);
        return place;
    }

    public Set<Place> PlaceDTOsToPlaces(Set<PlaceDTO> placeDTOs) {
        if (placeDTOs.isEmpty()) return Collections.emptySet();
        return placeDTOs.stream().map(this::PlaceDTOToPlace).collect(Collectors.toSet());
    }

    public PlaceDTO PlaceToPlaceDTO(Place place) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        return placeDTO;
    }

    public Set<PlaceDTO> PlacesToPlaceDTOs(Set<Place> places) {
        if (places.isEmpty()) return Collections.emptySet();
        return places.stream().map(this::PlaceToPlaceDTO).collect(Collectors.toSet());
    }
}
