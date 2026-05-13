package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import de.uniwue.dachs.fotolyrik_backend.DTO.KeywordPreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PlaceDTO;
import de.uniwue.dachs.fotolyrik_backend.DTO.PlacePreviewDTO;
import de.uniwue.dachs.fotolyrik_backend.model.Keyword;
import de.uniwue.dachs.fotolyrik_backend.model.Place;
import de.uniwue.dachs.fotolyrik_backend.repository.PlaceRepository;
import org.apache.catalina.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PlaceMapper {
    private final PlaceRepository placeRepository;

    public PlaceMapper(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place PlaceDTOToPlace(PlaceDTO placeDTO) {
        if (placeDTO == null) return null;
        if (placeDTO.getId() != null) {
            return placeRepository.findById(placeDTO.getId()).orElse(null);
        }
        Place place = new Place();
        place.setName(placeDTO.getName());
        place.setDescription(placeDTO.getDescription());
        place.setLatitude(placeDTO.getLatitude());
        place.setLongitude(placeDTO.getLongitude());
        placeRepository.save(place);
        return place;
    }

    public PlaceDTO PlaceToPlaceDTO(Place place) {
        if (place == null) return null;
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setId(place.getId());
        placeDTO.setName(place.getName());
        placeDTO.setDescription(place.getDescription());
        placeDTO.setLatitude(place.getLatitude());
        placeDTO.setLongitude(place.getLongitude());
        placeDTO.setBaseEntityFields(place);
        return placeDTO;
    }

    public Set<PlaceDTO> PlacesToPlaceDTOs(Set<Place> places) {
        return MapperUtils.mapSet(places, this::PlaceToPlaceDTO);
    }

    public Set<Place> PlaceDTOsToPlaces(Set<PlaceDTO> placeDTOs) {
        return MapperUtils.mapSet(placeDTOs, this::PlaceDTOToPlace);
    }

    public List<PlaceDTO> PlacesToPlaceDTOs(List<Place> places) {
        return MapperUtils.mapList(places, this::PlaceToPlaceDTO);
    }

    public List<Place> PlaceDTOsToPlaces(List<PlaceDTO> placeDTOs) {
        return MapperUtils.mapList(placeDTOs, this::PlaceDTOToPlace);
    }
    public Place PlacePreviewDTOToPlace(PlacePreviewDTO placePreviewDTO) {
        if (placePreviewDTO == null || placePreviewDTO.getId() == null) return null;
        return placeRepository.findById(placePreviewDTO.getId()).orElse(null);
    }

    public PlacePreviewDTO PlaceToPlacePreviewDTO(Place place) {
        if (place == null) return null;
        PlacePreviewDTO placePreviewDTO = new PlacePreviewDTO();
        placePreviewDTO.setId(place.getId());
        placePreviewDTO.setName(place.getName());
        return  placePreviewDTO;
    }

    public Set<Place> PlacePreviewDTOsToPlaces(Set<PlacePreviewDTO> placePreviewDTOs) {
        return MapperUtils.mapSet(placePreviewDTOs, this::PlacePreviewDTOToPlace);
    }
    public Set<PlacePreviewDTO> PlacesToPlacePreviewDTOs(Set<Place> places) {
        return MapperUtils.mapSet(places, this::PlaceToPlacePreviewDTO);
    }
    public List<Place> PlacePreviewDTOsToPlaces(List<PlacePreviewDTO> placePreviewDTOs) {
        return MapperUtils.mapList(placePreviewDTOs, this::PlacePreviewDTOToPlace);
    }
    public List<PlacePreviewDTO> PlacesToPlacePreviewDTOs(List<Place> places) {
        return MapperUtils.mapList(places, this::PlaceToPlacePreviewDTO);
    }
}
